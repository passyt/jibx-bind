# jibx-bind
Jibx for JDK8 with Javassist

Since BECL, which used by Jibx for code binding, is not updated and not support JDK8 any more. This project recodes the part of binding based on version 1.2.3 of Jibx. It should be compatible with latest version 1.2.5.

Known Issues:
 * Build package with 'mvn package' twice to load latest classes genarated by Javassist
 * Some deprecated attributes are not support any more such as 'format' in 'value' element

Any issues please email to me(passyt@gmail.com)
