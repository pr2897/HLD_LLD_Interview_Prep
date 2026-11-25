
import java.util.*;

// 1. CLOUD IDENTITY
enum Cloud {
    AWS, GCP, AZURE
}


// 2. ABSTRACT FACTORY
interface CloudProviderFactory {
    ComputeInstance createComputeInstance();
    StorageBucket createStorageBucket();
    MessageQueue createMessageQueue();
}

class AWSFactory implements CloudProviderFactory {

    private final CloudCredentials credentials;

    AWSFactory(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ComputeInstance createComputeInstance() {
        return new AWSComputeInstance(credentials);
    }

    public StorageBucket createStorageBucket() {
        return new AWSStorageBucket(credentials);
    }

    public MessageQueue createMessageQueue() {
        return new AWSMessageQueue(credentials);
    }
}

class GCPFactory implements CloudProviderFactory {

    private final CloudCredentials credentials;

    GCPFactory(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ComputeInstance createComputeInstance() {
        return new GCPComputeInstance(credentials);
    }

    public StorageBucket createStorageBucket() {
        return new GCPStorageBucket(credentials);
    }

    public MessageQueue createMessageQueue() {
        return new GCPMessageQueue(credentials);
    }
}

class AzureFactory implements CloudProviderFactory {

    private final CloudCredentials credentials;

    AzureFactory(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ComputeInstance createComputeInstance() {
        return new AzureComputeInstance(credentials);
    }

    public StorageBucket createStorageBucket() {
        return new AzureStorageBucket(credentials);
    }

    public MessageQueue createMessageQueue() {
        return new AzureMessageQueue(credentials);
    }
}


// 3. ABSTRACT PRODUCTS

interface ComputeInstance {
    ProvisionedResource provision(VMProvisioningSpec spec);
}

interface StorageBucket {
    ProvisionedResource create(String name, String region);
}

interface MessageQueue {
    ProvisionedResource create(String name, String region);
}


// 4. CONCRETE PRODUCTS

class AWSComputeInstance implements ComputeInstance {

    private final CloudCredentials credentials;

    AWSComputeInstance(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource provision(VMProvisioningSpec spec) {
        System.out.println("[AWS] Provisioning VM: " + spec);
        return new ProvisionedResource(spec.getName(), Cloud.AWS, spec.getRegion());
    }
}

class GCPComputeInstance implements ComputeInstance {

    private final CloudCredentials credentials;

    GCPComputeInstance(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource provision(VMProvisioningSpec spec) {
        System.out.println("[GCP] Provisioning VM: " + spec);
        return new ProvisionedResource(spec.getName(), Cloud.GCP, spec.getRegion());
    }
}

class AzureComputeInstance implements ComputeInstance {

    private final CloudCredentials credentials;

    AzureComputeInstance(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource provision(VMProvisioningSpec spec) {
        System.out.println("[AZURE] Provisioning VM: " + spec);
        return new ProvisionedResource(spec.getName(), Cloud.AZURE, spec.getRegion());
    }
}


class AWSStorageBucket implements StorageBucket {

    private final CloudCredentials credentials;

    AWSStorageBucket(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource create(String name, String region) {
        System.out.println("[AWS] Created bucket: " + name);
        return new ProvisionedResource(name, Cloud.AWS, region);
    }
}

class GCPStorageBucket implements StorageBucket {

    private final CloudCredentials credentials;

    GCPStorageBucket(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource create(String name, String region) {
        System.out.println("[GCP] Created bucket: " + name);
        return new ProvisionedResource(name, Cloud.GCP, region);
    }
}

class AzureStorageBucket implements StorageBucket {

    private final CloudCredentials credentials;

    AzureStorageBucket(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource create(String name, String region) {
        System.out.println("[AZURE] Created bucket: " + name);
        return new ProvisionedResource(name, Cloud.AZURE, region);
    }
}


class AWSMessageQueue implements MessageQueue {

    private final CloudCredentials credentials;

    AWSMessageQueue(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource create(String name, String region) {
        System.out.println("[AWS] Created queue: " + name);
        return new ProvisionedResource(name, Cloud.AWS, region);
    }
}

class GCPMessageQueue implements MessageQueue {

    private final CloudCredentials credentials;

    GCPMessageQueue(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource create(String name, String region) {
        System.out.println("[GCP] Created queue: " + name);
        return new ProvisionedResource(name, Cloud.GCP, region);
    }
}

class AzureMessageQueue implements MessageQueue {

    private final CloudCredentials credentials;

    AzureMessageQueue(CloudCredentials credentials) {
        this.credentials = credentials;
    }

    public ProvisionedResource create(String name, String region) {
        System.out.println("[AZURE] Created queue: " + name);
        return new ProvisionedResource(name, Cloud.AZURE, region);
    }
}


// 5. BUILDER

final class VMProvisioningSpec {

    private final String name;
    private final String region;
    private final int cpuCount;
    private final int memoryGB;
    private final Integer diskSizeGB;
    private final List<String> tags;
    private final boolean spotInstance;
    private final String sshKey;

    private VMProvisioningSpec(Builder b) {
        this.name = b.name;
        this.region = b.region;
        this.cpuCount = b.cpuCount;
        this.memoryGB = b.memoryGB;
        this.diskSizeGB = b.diskSizeGB;
        this.tags = List.copyOf(b.tags);
        this.spotInstance = b.spotInstance;
        this.sshKey = b.sshKey;
    }

    public String getName() { return name; }
    public String getRegion() { return region; }

    public static class Builder {

        private final String name;
        private final String region;
        private final int cpuCount;
        private final int memoryGB;

        private Integer diskSizeGB;
        private List<String> tags = new ArrayList<>();
        private boolean spotInstance;
        private String sshKey;

        public Builder(String name, String region, int cpuCount, int memoryGB) {
            this.name = name;
            this.region = region;
            this.cpuCount = cpuCount;
            this.memoryGB = memoryGB;
        }

        public Builder diskSize(int size) {
            this.diskSizeGB = size;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = new ArrayList<>(tags);
            return this;
        }

        public Builder spotInstance(boolean spot) {
            this.spotInstance = spot;
            return this;
        }

        public Builder sshKey(String key) {
            this.sshKey = key;
            return this;
        }

        public VMProvisioningSpec build() {

            if (name == null || name.isBlank())
                throw new IllegalArgumentException("Name required");

            if (region == null || region.isBlank())
                throw new IllegalArgumentException("Region required");

            if (cpuCount <= 0)
                throw new IllegalArgumentException("Invalid CPU count");

            if (memoryGB <= 0)
                throw new IllegalArgumentException("Invalid memory");

            if (diskSizeGB != null && diskSizeGB <= 0)
                throw new IllegalArgumentException("Invalid disk size");

            return new VMProvisioningSpec(this);
        }
    }

    @Override
    public String toString() {
        return "VMProvisioningSpec{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", cpuCount=" + cpuCount +
                ", memoryGB=" + memoryGB +
                ", diskSizeGB=" + diskSizeGB +
                ", tags=" + tags +
                ", spotInstance=" + spotInstance +
                ", sshKey='" + sshKey + '\'' +
                '}';
    }
}


// 6. CREDENTIALS

class CloudCredentials {

    private CloudCredentials() {
        System.out.println("Loading cloud credentials once...");
    }

    private static class Holder {
        private static final CloudCredentials INSTANCE =
                new CloudCredentials();
    }

    public static CloudCredentials getInstance() {
        return Holder.INSTANCE;
    }
}


// 7. RESOURCE REGISTRY

record ProvisionedResource(
        String resourceName,
        Cloud cloud,
        String region
) {}

class ResourceRegistry {

    private final List<ProvisionedResource> resources =
            new ArrayList<>();

    public synchronized void register(ProvisionedResource resource) {
        resources.add(resource);
    }

    public synchronized List<ProvisionedResource> getResources() {
        return List.copyOf(resources);
    }

    public void printSummary() {
        getResources().forEach(resource ->
                System.out.printf(
                        "Resource: %s | Cloud: %s | Region: %s%n",
                        resource.resourceName(),
                        resource.cloud(),
                        resource.region()
                )
        );
    }
}


// 8. CLOUD CONFIGURATION

class CloudConfiguration {

    public static CloudProviderFactory getFactory(
            String provider,
            CloudCredentials credentials
    ) {

        return switch (provider.toLowerCase(Locale.ROOT)) {
            case "aws" -> new AWSFactory(credentials);
            case "gcp" -> new GCPFactory(credentials);
            case "azure" -> new AzureFactory(credentials);
            default -> throw new IllegalArgumentException(
                    "Unsupported cloud provider: " + provider
            );
        };
    }
}


// 9. PROVISIONING SERVICE
class CloudProvisioningService {

    private final CloudProviderFactory factory;
    private final ResourceRegistry registry;

    CloudProvisioningService(
            CloudProviderFactory factory,
            ResourceRegistry registry
    ) {
        this.factory = factory;
        this.registry = registry;
    }

    public void provisionVM(VMProvisioningSpec spec) {
        ProvisionedResource resource =
                factory.createComputeInstance().provision(spec);

        registry.register(resource);
    }

    public void createBucket(String name, String region) {
        ProvisionedResource resource =
                factory.createStorageBucket().create(name, region);

        registry.register(resource);
    }

    public void createQueue(String name, String region) {
        ProvisionedResource resource =
                factory.createMessageQueue().create(name, region);

        registry.register(resource);
    }
}


// 10. MAIN
public class Code_H1 {

    public static void main(String[] args) {

        // Configuration: one-line cloud switch
        String provider = "aws";

        // Load credentials once
        CloudCredentials credentials = CloudCredentials.getInstance();

        // Select cloud factory
        CloudProviderFactory factory =
                CloudConfiguration.getFactory(provider, credentials);

        // Shared registry
        ResourceRegistry registry = new ResourceRegistry();

        // Provisioning service
        CloudProvisioningService service =
                new CloudProvisioningService(factory, registry);

        // Build VM specification
        VMProvisioningSpec spec =
                new VMProvisioningSpec.Builder(
                        "web1", "asia-south1", 4, 16
                )
                .diskSize(100)
                .tags(List.of("prod"))
                .spotInstance(false)
                .build();

        // Provision resources
        service.provisionVM(spec);
        service.createBucket("prod-data", "asia-south1");
        service.createQueue("email-events", "asia-south1");

        // Print summary
        System.out.println("\n===== RESOURCE SUMMARY =====");
        registry.printSummary();
    }
}
