package ch.cern.cmms.plugins;

public class SharedPluginImpl implements SharedPlugin {

    @Override
    public String getBaseUrl() {
        return "/WSHub/REST/apis";
    }
}
