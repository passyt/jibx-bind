package org.jibx.binding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.jibx.binding.classes.javassist.BindingContext;
import org.jibx.binding.util.ClassUtils;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class Compile {

	/**
	 * test load modified classes to validate
	 */
	private boolean load;
	/**
	 * report binding details and results
	 */
	private boolean verbose;
	/**
	 * output debug classes
	 */
	private boolean debug;

	public Compile() {
		super();
	}

	public Compile(boolean load, boolean verbose) {
		this(load, verbose, false);
	}

	public Compile(boolean load, boolean verbose, boolean debug) {
		super();
		this.load = load;
		this.verbose = verbose;
		this.debug = debug;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Compile a set of bindings using supplied classpaths.
	 *
	 * @param paths
	 *            list of paths for loading classes
	 * @param files
	 *            list of binding definition files
	 * @exception JiBXException
	 *                if error in processing the binding definition
	 */
	public void compile(String[] pathes, String... files) throws JiBXException {
		if (verbose) {
			System.out.println("Running binding compiler version " + JibxConst.COMPILER_DISTRIBUTION + ", debug " + debug);
		}
		List<IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> classFiles = new ArrayList<IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>>();
		for (String filePath : files) {
			IBindingContext bindingContext = new BindingContext(getOutputPath(pathes), filePath, pathes, verbose, debug);
			classFiles.addAll(bindingContext.generateCode());
		}

		for (IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField> classFile : classFiles) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				classFile.write(out);
			} catch (Exception e) {
				throw new JiBXException("Output byte codes failed", e);
			}

			byte[] bytes = out.toByteArray();
			if (verbose) {
				System.out.println(classFile.getClassName() + " output file size is " + bytes.length + " bytes");
			}

			if (load) {
				DirectLoader cloader = new DirectLoader(pathes);
				Class<?> clazz = cloader.load(classFile.getClassName(), bytes);
				if (verbose) {
					System.out.println(ClassUtils.trace(clazz));
				}
			}
		}
	}

	private String getOutputPath(String[] pathes) throws JiBXException {
		for (String path : pathes) {
			File file = new File(path);
			if (file.isDirectory()) {
				return path;
			}
		}

		throw new JiBXException("No directory found in the pathes");
	}

	/**
	 * Direct class loader. This is optionally used for test loading the
	 * modified class files to make sure they're still valid.
	 */
	protected static class DirectLoader extends URLClassLoader {

		public DirectLoader(String[] paths) {
			super(toUrls(paths), DirectLoader.class.getClassLoader());
		}

		protected Class<?> load(String name, byte[] data) {
			return defineClass(name, data, 0, data.length);
		}

		private static URL[] toUrls(String[] paths) {
			try {
				URL[] urls = new URL[paths.length];
				for (int i = 0; i < urls.length; i++) {
					urls[i] = new File(paths[i]).toURI().toURL();
				}
				return urls;
			} catch (MalformedURLException e) {
				throw new IllegalStateException(e);
			}
		}
	}

}
