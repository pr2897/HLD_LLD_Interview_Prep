
class UserProfile {
    private final String name;
    private final String email;
    private final String phone;
    private final String city;
    private final String bio;
    private final String profilePicUrl;
    private final boolean newsletterOptIn;


    private UserProfile(Builder b) {
        this.name = b.name;
        this.email = b.email;
        this.phone = b.phone;
        this.city = b.city;
        this.bio = b.bio;
        this.profilePicUrl = b.profilePicUrl;
        this.newsletterOptIn = b.newsletterOptIn;
    }

    // TODO: Task 1
    // private UserProfile(String name, String email) { ...}
    // private UserProfile(String name, String email, String phone) { ...}
    // private UserProfile(String name, String email, String phone, String city, String bio, String profilePicUrl, String newsletterOptIn) { ... }


    // TODO: Task 2
    public static class Builder {
        // mandatory
        private String name;
        private String email;

        // optional
        private String phone;
        private String city;
        private String bio;
        private String profilePicUrl;
        private boolean newsletterOptIn;

        public Builder(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder city(String city) { this.city = city; return this; }
        public Builder bio(String bio) { this.bio = bio; return this; }
        public Builder profilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; return this; }
        public Builder newsletterOptIn(boolean newsletterOptIn) { this.newsletterOptIn = newsletterOptIn; return this; }

        // TODO: Task 3
        public UserProfile build() {
            // validation
            if(!email.contains("@")) throw new IllegalStateException("email has no @");
            if(name.isBlank()) throw new IllegalStateException("name is blank");


            // TODO: Task 4
            /**
             * the reason why we do validations in build is because, this is the consolidation point where we create 
             * the instance of UserProfile. if we do that in setter, we loose the validation if it contains the combination
             * of lets say city and phone (not good example). so in that case if we do validation in setter, we reject early,
             * hence we do the validation in build method.
             */

            // object creation
            return new UserProfile(this);
        }
    }
}

public class Code_E2 {
    public static void main(String[] args) {
        // //  eg: 1
        // UserProfile piyush = new UserProfile.Builder("Piyush", "test.com")
        // .bio("traveller")
        // .city("bihar")
        // .build();

        // // eg: 2
        // UserProfile piyush = new UserProfile.Builder("", "test@com")
        // .bio("traveller")
        // .city("bihar")
        // .build();

        UserProfile piyush = new UserProfile.Builder("Piyush", "test@com")
        .bio("traveller")
        .city("bihar")
        .build();

        System.out.println(piyush);
    }
}
