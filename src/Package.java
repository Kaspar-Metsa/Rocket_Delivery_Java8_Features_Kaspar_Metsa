
public class Package {

    private int weight; // between 1-80kg
    private PostOfficeLocation sourcePostOfficeLocation;
    private PostOfficeLocation destinationPostOfficeLocation;

    public Package(int weight, PostOfficeLocation sourcePostOfficeLocation, PostOfficeLocation destinationPostOfficeLocation) {
        this.weight = weight;
        this.sourcePostOfficeLocation = sourcePostOfficeLocation;
        this.destinationPostOfficeLocation = destinationPostOfficeLocation;
    }

    public int getWeight() {
        return weight;
    }

    public PostOfficeLocation getSourcePostOfficeLocation() {
        return sourcePostOfficeLocation;
    }

    public PostOfficeLocation getDestinationPostOfficeLocation() {
        return destinationPostOfficeLocation;
    }

    
}
