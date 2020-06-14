package cf.nytrux.ffa;

import cf.nytrux.ffa.manager.ConfigurationManager;

public interface IRequiresConfigurationManager {
    final static ConfigurationManager configurationManager = FreeForAll.getInstance().getConfigurationManager();
}
