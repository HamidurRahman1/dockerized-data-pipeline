package org.hrahman.ddp.vault;

import com.bettercloud.vault.SslConfig;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;

import java.util.Properties;

public final class VaultPropertyRetriever {

    private final Vault vault;

    public VaultPropertyRetriever() throws VaultException {

        String vaultAddress = System.getenv("VAULT_ADDR");
        String vaultToken = System.getenv("VAULT_TOKEN");

        if (vaultAddress == null || vaultAddress.trim().isEmpty()) {
            throw new VaultException("VAULT_ADDR key is missing from environment");
        }

        if (vaultToken == null || vaultToken.trim().isEmpty()) {
            throw new VaultException("VAULT_TOKEN key is missing from environment");
        }

        final VaultConfig config = new VaultConfig()
                        .engineVersion(1)
                        .address(vaultAddress)
                        .token(vaultToken)
                        .openTimeout(5)
                        .readTimeout(10)
                        .sslConfig(new SslConfig().verify(false))
                        .build();

        vault = new Vault(config);
    }

    public Properties getVaultProperties(String path) throws VaultException {

        LogicalResponse logicalResponse = vault.logical().read(path);

        if (logicalResponse == null) {
            throw new VaultException("Vault response is null for path: " + path);
        }

        if (logicalResponse.getData() == null) {
            throw new VaultException("Vault secret not found in path: " + path);
        }

        Properties properties = new Properties();
        properties.putAll(logicalResponse.getData());

        return properties;
    }
}
