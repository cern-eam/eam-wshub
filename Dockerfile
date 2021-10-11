FROM jboss/wildfly:22.0.1.Final

ADD docker/standalone.xml /opt/jboss/wildfly/standalone/configuration/
ADD docker/cacerts /etc/pki/ca-trust/extracted/java/
ADD wshubear/target/wshub.ear /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080/tcp
EXPOSE 9090/tcp