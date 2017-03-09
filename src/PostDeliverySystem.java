//Sync:
//https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html


import java.util.ArrayList;


public class PostDeliverySystem {
    
    public static int t = 0;
    
    public static void main(String[] args) {

        // create post offices for all locations (planets and moons)
        ArrayList<PostOffice> postOffices = new ArrayList<>();
        //The array postOffices is full of enum constants of all planets
        for (int i = 0; i < 11; i++) {
            PostOffice office = new PostOffice(PostOfficeLocation.getLocation(i));
            postOffices.add(office);
        }

        // adding 20 regular rockets
        //getRandomRegularPlanet() gives me the enum constant(that is of PostOfficeLocation type) and I set that for the start location
        for (int i = 0; i < 20; i++) {
            Thread regularRocket = new Thread(new RegularRocket(getRandomRegularPlanet(), postOffices, "Regular Rocket " + i));
            regularRocket.start();
        }

        // adding 5 heat resistant rockets
        //getRandomSpecialPlanet() gives me the enum constant(that is of PostOfficeLocation type) and I set that for the start location

        for (int i = 0; i < 5; i++) {
            HeatResistantRocket lasHeatResistantRocket = new HeatResistantRocket(getRandomSpecialPlanet(), postOffices, "Heat Resistant Rocket " + i);
            new Thread(lasHeatResistantRocket).start();
        }

        // Create and start the package creator
        Thread packageCreator = new Thread(new PackageCreator(postOffices));
        packageCreator.start();

        // wait until the package creator finish its task
        try {
            //join waits for the thread to die
            packageCreator.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Package Creation finished");

        //Here the packages are already generated and 99,99% packages have been delivered, only couple
        //packages are left to be delivered, we need to somehow understand when those last packages
        //have been delivered, this is how we do it:
        //
        //This loop checks whether the rockets are still delivering the packages.
        // If the delivered package count has not been changed after 100ms,
        // it implies that the rockets are not delivering packages.
        // If the rockets are going to exchange the cosmic ray indicator,
        // there is enough time to do that within the 100ms interval because it takes only
        // 15ms to go between planets. You can ensure that by looking at the output also.
        // No rockets other than the post round rocket are flying after printing the message
        // "All Rockets are now waiting...".

        int totalDeliveredPackageCount;
        int currentCount = 0;
        do {
            totalDeliveredPackageCount = currentCount;
            currentCount = 0;
            for (int i = 0; i < postOffices.size(); i++) {
                currentCount += postOffices.get(i).getReceivedPackageCount();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } while (totalDeliveredPackageCount != currentCount);

        System.out.println("All Rockets are now waiting...");
     
        
        // start the post round
        // Post round means a new single rocket visits all planets
        //postOffices contain all 11 planets
        doPostRound(postOffices);
        System.out.println("All packages were delivered successfully!");
        
        // How many packages have been sent from Pluto to Earth?
        //getIndex returns number 2 which corresponds to Earth's position in postOffices' array(number 2)
        PostOffice earth = postOffices.get(PostOfficeLocation.getIndex(PostOfficeLocation.EARTH));
        int count = earth.getSuitablePackageCount((packet) -> packet.getSourcePostOfficeLocation()==PostOfficeLocation.PLUTO);
        System.out.println("There are "+count+" packages from Pluto to Earth");



        
        System.out.println("System finished");
        System.exit(0);
    }
    
    public static void doPostRound(ArrayList<PostOffice> postOffices){
        //I start post round from Mercury(randomly chosen planet 0)
        PostRoundRocket postRoundRocket = new PostRoundRocket(PostOfficeLocation.getLocation(0), postOffices);
        
        System.out.println("Starting post round with " + postRoundRocket.getName());
        
        boolean finished = false;

        do {
            //First cycle this variable will be 0
            PostOfficeLocation currentLocation = postRoundRocket.getCurrentLocation();
            int indexStart = PostOfficeLocation.getIndex(currentLocation);
            int skipped = 0;
            for (int i = indexStart; i < 12; i++) {
                //Here I just restart the loop, it's not a very effective method of doing this but it works, it will start the whole loop even if only 1 planet has packages left
                if(i==11){
                    i=0;
                }
                //???
                skipped++;
                //???
                if(i==indexStart&&skipped!=1){
                    finished = true;
                    break;
                }
                //This just iterates over ALL post offices chosen in the order they are in PostOfficeLocation class
                PostOffice postOffice = postOffices.get(i);

                // check if this post office has any undelivered packages
                int undelivered = postOffice.getRemainingPackageCount();
                //This checks if THIS planet the rocket is currently on, has any remaining packets left to be delivered. if it has it will load the packets onto rocket
                //fly to the first planet and offload them AND BREAK THE FOR LOOP. It breaks so i can start again.
                if (undelivered > 0) {
                    try {
                        postOffice.loadPacketsFinally(postRoundRocket);
                        postOffice.service(postRoundRocket);
                        //The rocket got packages from loadPacketsFinally method, now i select the destinationPlanet of the first package as my PostRocket's new destination
                        PostOffice toPostOffice = postOffices.get(PostOfficeLocation.getIndex(postRoundRocket.packages.get(0).getDestinationPostOfficeLocation()));
                        //I fly to that planet
                        postRoundRocket.fly(toPostOffice.getLocation());
                        //requirement to wait 15ms between all travels
                        Thread.sleep(15);
                        postRoundRocket.setCurrentLocation(toPostOffice.getLocation());
                        //I offload rockets in the new rocket
                        toPostOffice.offLoadPackets(postRoundRocket);
                        //This breaks out of the for loop but not from the do/while loop
                        break;
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                
                

            }

            
            
        } while (!finished);
    }

    public static PostOfficeLocation getRandomRegularPlanet() {
        //Math.random() returns a double value with a positive sign, greater than or equal to 0.0 and less than 1.0.
        //So index can be from 0(0*11=0) to 10(0.99*11=10.99=10)
        int index = (int) (Math.random() * 11);

        //Mercury index 0 is not a regular planet so I select Saturn instead(randomly)
        if (index == 0) {
            index = 5;
        }
        //Venus index 1 is not a regular planet so I select Io instead(randomly)
        if (index == 1) {
            index = 10;
        }

        return PostOfficeLocation.getLocation(index);
    }

    public static PostOfficeLocation getRandomSpecialPlanet() {
        
        while(true){
            //Index can be from 0(0*11=0) to 10(0.99*11=10.99=10)
            int index = (int) (Math.random() * 11);
            //This gets the enum constant
            PostOfficeLocation special = PostOfficeLocation.getLocation(index);
            //Returns true if special is Mercury or Venus
            if(PostOffice.isPlanetHot(special)){
                //returns enum constant of PostOfficeLocation type
                return special;
            }
        }
        
    }

}
