
import java.util.ArrayList;
import java.util.Random;

public class PackageCreator implements Runnable {

    private static int totalCreatedPackageCount = 0;
    private ArrayList<PostOffice> postOffices;

    public PackageCreator(ArrayList<PostOffice> postOffices) {
        this.postOffices = postOffices;
    }

    
    @Override
    public void run() {
        try {
            while (totalCreatedPackageCount < 1500) {
                // put the package to the back of the queue of the source post office. 
                Package p = createPackage();
                //Here i put the package with random source and destination planet into the packagesToSend List
                postOffices.get(PostOfficeLocation.getIndex(p.getSourcePostOfficeLocation())).addPackageToSend(p);
                //I keep the count to know when to come out of the loop
                totalCreatedPackageCount++;
                // There is 3ms between creating every package
                Thread.sleep(3);
            }
            
                        
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //I use static factory method to create a package
    private Package createPackage() {

        // find random distinct source and destination post offices 
        Random r = new Random();
        //Nextint gives us a value between 0 (inclusive) and 11 (exclusive)
        int sourceIndex = r.nextInt(11);
        int destinationIndex = r.nextInt(11);
        //We don't want the same source and to planet
        while (sourceIndex == destinationIndex) {
            destinationIndex = r.nextInt(11);
        }

        PostOfficeLocation sourceLocation = PostOfficeLocation.getLocation(sourceIndex);
        PostOfficeLocation destinationLocation = PostOfficeLocation.getLocation(destinationIndex);

        // get a random weight between 1-80kg
        int weight = 1 + r.nextInt(80);
        
        // create the package and return it
        return new Package(weight, sourceLocation, destinationLocation);
    }

}
