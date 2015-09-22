package org.jibx.binding.classes.javassist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javassist.ClassPool;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.IBuildFactory;
import org.jibx.binding.IClassFile;
import org.jibx.binding.IConstructor;
import org.jibx.binding.IField;
import org.jibx.binding.IMethod;
import org.jibx.binding.JibxConst;
import org.jibx.binding.classes.javassist.builder.ClassGenerateDefinition;
import org.jibx.binding.classes.javassist.builder.MugeAdapterClassBuilder;
import org.jibx.binding.classes.javassist.builder.NamespaceDefinition;
import org.jibx.binding.model.BindingChild;
import org.jibx.binding.model.BindingElement;
import org.jibx.binding.model.MappingElementBase;
import org.jibx.binding.model.NamespaceElement;
import org.jibx.binding.model.NamespaceStyle;
import org.jibx.binding.model.TemplateChild;
import org.jibx.binding.parse.BindingXmlParser;
import org.jibx.binding.util.JibxUtils;
import org.jibx.binding.util.Joiner;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class BindingContext implements IBindingContext {

	private final String outputPath;
	private final boolean verbose;
	private final boolean debug;

	private final BindingElement bindingRoot;
	@SuppressWarnings("rawtypes")
	private final IBuildFactory buildFactory;
	private String defaultElementNamespace;
	private String defaultAttributeNamespace;
	private Map<String, NamespaceDefinition> namespaces;

	private final Map<String, IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> classFiles = new LinkedHashMap<String, IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>>();
	private final Map<String, MappingElementBase> mappingElements = new HashMap<String, MappingElementBase>();
	private final Map<String, ClassGenerateDefinition> classGenerateDefinitions = new HashMap<String, ClassGenerateDefinition>();

	/**
	 * blob of class or type names for mappings
	 */
	private final List<String> mappednames = new ArrayList<String>();
	/**
	 * unmarshaller class names blob (<code>null</code> if output-only binding)
	 */
	private final List<String> umarnames = new ArrayList<String>();
	/**
	 * marshaller class names blob (<code>null</code> if input-only binding)
	 */
	private final List<String> marnames = new ArrayList<String>();
	/**
	 * globally-mapped element names blob
	 */
	private final List<String> gmapnames = new ArrayList<String>();
	/**
	 * globally-mapped element namespaces blob
	 */
	private final List<Integer> gmapuris = new ArrayList<Integer>();
	/**
	 * abstract mapping details blob
	 */
	private final List<String> abmapdetails = new ArrayList<String>();
	/**
	 * abstract mapping namespace indexes blob
	 */
	private final List<Integer> abmapnss = new ArrayList<Integer>();

	private transient Stack<MappingElementBase> mappingStack = new Stack<MappingElementBase>();

	public BindingContext(String outputPath, String bindingFilePath, String[] classpaths, boolean verbose, boolean debug) {
		super();
		this.outputPath = outputPath;
		this.verbose = verbose;
		this.debug = debug;

		try {
			this.bindingRoot = new BindingXmlParser(bindingFilePath).parse();

			ClassPool classPool = ClassPool.getDefault();
			if (classpaths != null) {
				for (String classpath : classpaths) {
					classPool.appendClassPath(classpath);
				}
			}
			this.buildFactory = new JavassistBuildFactory(classPool, debug);

			init();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	protected void init() throws JiBXException {
		Map<String, NamespaceDefinition> namespaces = new LinkedHashMap<String, NamespaceDefinition>();
		int index = 0;
		namespaces.put("", new NamespaceDefinition("", "", null, index++));
		namespaces.put("http://www.w3.org/XML/1998/namespace", new NamespaceDefinition("http://www.w3.org/XML/1998/namespace", "xml", NamespaceStyle.Elements, index++));
		namespaces.put("http://www.w3.org/2001/XMLSchema-instance", new NamespaceDefinition("http://www.w3.org/2001/XMLSchema-instance", "xsi", NamespaceStyle.Elements, index++));

		for (Iterator<BindingChild> bindingChildIterator = bindingRoot.bindingChildIterator(true); bindingChildIterator.hasNext();) {
			BindingChild bindingChild = bindingChildIterator.next();
			if (bindingChild instanceof NamespaceElement) {
				NamespaceElement namespaceElement = (NamespaceElement) bindingChild;
				if (NamespaceStyle.All == namespaceElement.getDefaultStyle() || NamespaceStyle.Elements == namespaceElement.getDefaultStyle()) {
					this.defaultElementNamespace = namespaceElement.getUri();
				}
				if (NamespaceStyle.All == namespaceElement.getDefaultStyle() || NamespaceStyle.Attributes == namespaceElement.getDefaultStyle()) {
					this.defaultAttributeNamespace = namespaceElement.getUri();
				}
				if (!namespaces.containsKey(namespaceElement.getUri())) {
					namespaces.put(namespaceElement.getUri(), new NamespaceDefinition(namespaceElement.getUri(), namespaceElement.getPrefix(), namespaceElement.getDefaultStyle(),
							index++));
				}
			} else if (bindingChild instanceof MappingElementBase) {
				MappingElementBase mappingElement = (MappingElementBase) bindingChild;
				mappingElements.put(mappingElement.getIdentityName(), mappingElement);
				for (Iterator<TemplateChild> templateIterator = mappingElement.templateChildIterator(); templateIterator.hasNext();) {
					TemplateChild templateChild = templateIterator.next();
					if (templateChild instanceof NamespaceElement) {
						NamespaceElement namespaceElement = (NamespaceElement) templateChild;
						if (!namespaces.containsKey(namespaceElement.getUri())) {
							namespaces.put(namespaceElement.getUri(),
									new NamespaceDefinition(namespaceElement.getUri(), namespaceElement.getPrefix(), namespaceElement.getDefaultStyle(), index++));
						}
					}
				}
			}
		}

		this.namespaces = namespaces;
		this.buildFactory.init(bindingRoot);
	}

	@Override
	public String getMugeAdapterClassName() {
		String prefix = JibxConst.GENERATE_PREFIX + bindingRoot.getName();
		String packagePrefix = bindingRoot.getFacotryPackage(bindingRoot);
		if (packagePrefix.length() > 0) {
			packagePrefix = packagePrefix + ".";
		}
		return packagePrefix + prefix + JibxConst.MUNGEADAPTER_SUFFIX;
	}

	@Override
	public Collection<IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> generateCode() throws JiBXException {
		new MugeAdapterClassBuilder(getMugeAdapterClassName(), bindingRoot.getFormats(), this).build();

		for (Iterator<BindingChild> bindingChildIterator = bindingRoot.bindingChildIterator(true); bindingChildIterator.hasNext();) {
			BindingChild bindingChild = bindingChildIterator.next();
			if (bindingChild instanceof MappingElementBase) {
				MappingElementBase mappingElement = (MappingElementBase) bindingChild;
				this.buildFactory.getMappingBuilder().build(mappingElement, this);
			}
		}

		this.buildFactory.getFactoryBuilder().build(this);
		return classFiles.values();
	}

	@Override
	public boolean isBuild(String className) {
		return classFiles.containsKey(className);
	}

	@Override
	public Collection<IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> getClassFiles() {
		return classFiles.values();
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public BindingElement getBindingRoot() {
		return this.bindingRoot;
	}

	@Override
	public MappingElementBase getMappingElement(String className) {
		return mappingElements.get(className);
	}

	@Override
	public ClassGenerateDefinition getClassDefinition(String typeName, String className) {
		ClassGenerateDefinition classGenerateDefinition = classGenerateDefinitions.get(typeName);
		if (className == null) {
			throw new IllegalStateException("ClassGenerateDefinition not found by type [" + typeName + "]");
		}

		if (classGenerateDefinition == null) {
			classGenerateDefinition = new ClassGenerateDefinition(bindingRoot.getName(), className);
			classGenerateDefinitions.put(typeName, classGenerateDefinition);
		}

		return classGenerateDefinition;
	}

	@Override
	public ClassGenerateDefinition nextClassDefinition(String className) {
		return new ClassGenerateDefinition(bindingRoot.getName(), className);
	}

	@Override
	public String currentDefaultElementNamespace() {
		MappingElementBase currentMapping = mappingStack.peek();
		if (currentMapping == null) {
			return this.defaultElementNamespace;
		}

		for (Iterator<TemplateChild> it = currentMapping.templateChildIterator(); it.hasNext();) {
			TemplateChild bindingChild = it.next();
			if (bindingChild instanceof NamespaceElement) {
				NamespaceElement namespaceElement = (NamespaceElement) bindingChild;
				if (NamespaceStyle.All == namespaceElement.getDefaultStyle() || NamespaceStyle.Elements == namespaceElement.getDefaultStyle()) {
					return namespaceElement.getUri();
				}
			}
		}

		return this.defaultElementNamespace;
	}

	@Override
	public String currentDefaultAttributeNamespace() {
		MappingElementBase currentMapping = mappingStack.peek();
		if (currentMapping == null) {
			return this.defaultAttributeNamespace;
		}

		for (Iterator<TemplateChild> it = currentMapping.templateChildIterator(); it.hasNext();) {
			TemplateChild bindingChild = it.next();
			if (bindingChild instanceof NamespaceElement) {
				NamespaceElement namespaceElement = (NamespaceElement) bindingChild;
				if (NamespaceStyle.All == namespaceElement.getDefaultStyle() || NamespaceStyle.Attributes == namespaceElement.getDefaultStyle()) {
					return namespaceElement.getUri();
				}
			}
		}

		return this.defaultAttributeNamespace;
	}

	@Override
	public Map<String, NamespaceDefinition> getNamespaces() {
		return this.namespaces;
	}

	@Override
	public String getOutputPath() {
		return this.outputPath;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IBuildFactory getBuildFactory() {
		return this.buildFactory;
	}

	@Override
	public void addClassFile(IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField> classFile) {
		this.classFiles.put(classFile.getClassName(), classFile);
	}

	@Override
	public void addMappingClassItem(String className, String elementName, String umarshallClassName, String marshallClassName, Integer nsIndex) {
		mappednames.add(className);
		gmapnames.add(elementName == null ? "" : elementName);
		umarnames.add(umarshallClassName == null ? "" : umarshallClassName);
		marnames.add(marshallClassName == null ? "" : marshallClassName);
		gmapuris.add(nsIndex);
	}

	@Override
	public void addAbstractMappingClassItem(String typeName, String className, String createMethod, String completeMethod, String prepareMethod, String attributePresentTestMethod,
			String attributeUnmarshalMethod, String attributeMarshalMethod, String contentPresentTestMethod, String contentUnmarshalMethod, String contentMarshalMethod,
			List<Integer> nsIndexes) {
		abmapdetails.add(JibxUtils.buildClassNamesBlob(new String[] { typeName, className, combine(className, createMethod), combine(className, completeMethod),
				combine(className, prepareMethod), combine(className, attributePresentTestMethod), combine(className, attributeUnmarshalMethod),
				combine(className, attributeMarshalMethod), combine(className, contentPresentTestMethod), combine(className, contentUnmarshalMethod),
				combine(className, contentMarshalMethod) }));

		if (nsIndexes == null || nsIndexes.isEmpty()) {
			abmapnss.add(1);
		} else {
			abmapnss.add(nsIndexes.size() + 1);
			for (int index : nsIndexes) {
				abmapnss.add(index + 1);
			}
		}
	}

	@Override
	public void startMapping(MappingElementBase mapping) {
		mappingStack.push(mapping);
	}

	@Override
	public void endMapping(MappingElementBase mapping) {
		if (mappingStack.peek() == mapping) {
			mappingStack.pop();
		}
	}

	public String getMappednames() {
		return JibxUtils.buildClassNamesBlob(mappednames);
	}

	public String getUmarnames() {
		return JibxUtils.buildClassNamesBlob(umarnames);
	}

	public String getMarnames() {
		return JibxUtils.buildClassNamesBlob(marnames);
	}

	public String getGmapnames() {
		return JibxUtils.buildClassNamesBlob(gmapnames);
	}

	public List<Integer> getGmapuris() {
		return gmapuris;
	}

	public String getAbmapdetails() {
		return Joiner.on("|").join(abmapdetails);
	}

	public List<Integer> getAbmapnss() {
		return abmapnss;
	}

	private static String combine(String prefix, String postfix) {
		if (postfix == null) {
			return null;
		}

		return prefix + "." + postfix;
	}

}