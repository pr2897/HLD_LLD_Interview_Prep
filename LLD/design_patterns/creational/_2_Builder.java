import java.util.*;

class User {
    private String name;
    private int rollNumber;
    private String address;
    private List<Integer> marks;


    private User(Builder b) {
        this.name = b.name;
        this.rollNumber = b.rollNumber;
        this.address = b.address;
        this.marks = new ArrayList<>(b.marks);
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", rollNumber=" + rollNumber + ", address=" + address + ", marks=" + marks + "]";
    }

    /* it relaxes constructor explosion and provides immutability */
    public static class Builder {
        private String name;
        private int rollNumber;
        private String address;
        private List<Integer> marks;
        
        // mandatory parameters (in constructors)
        public Builder(String name) {
            this.name = name;
            this.marks = new ArrayList<>();
        }

        // optional parameters (via chaining)
        public Builder withRollNumber(int roll) { this.rollNumber = roll; return this; }
        public Builder withAddress(String address) { this.address = address; return this; }
        public Builder withMarks(List<Integer> marks) {this.marks = marks; return this; }

        public User build() {
            return new User(this);
        }
    }
}

public class _2_Builder {
    public static void main(String[] args) {
        User me = new User.Builder("Piyush")
        .withAddress("india")
        .withRollNumber(100)
        .build();

        System.out.println(me);
    }
}
