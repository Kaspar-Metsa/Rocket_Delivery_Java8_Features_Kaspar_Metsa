// to keep track of post office locations easily

public enum PostOfficeLocation {

    MERCURY,
    VENUS,
    EARTH,
    MARS,
    JUPITER,
    SATURN,
    URANUS,
    NEPTUNE,
    PLUTO,
    MOON,
    IO;

    // convert index to a Post office location
    public static PostOfficeLocation getLocation(int index) {
        switch (index) {
            case 0:
                return PostOfficeLocation.MERCURY;
            case 1:
                return PostOfficeLocation.VENUS;
            case 2:
                return PostOfficeLocation.EARTH;
            case 3:
                return PostOfficeLocation.MARS;
            case 4:
                return PostOfficeLocation.JUPITER;
            case 5:
                return PostOfficeLocation.SATURN;
            case 6:
                return PostOfficeLocation.URANUS;
            case 7:
                return PostOfficeLocation.NEPTUNE;
            case 8:
                return PostOfficeLocation.PLUTO;
            case 9:
                return PostOfficeLocation.MOON;
            case 10:
                return PostOfficeLocation.IO;
            default:
                return null;
        }
    }

    // convert Post office location to an index
    public static int getIndex(PostOfficeLocation location) {
        switch (location) {
            case MERCURY:
                return 0;
            case VENUS:
                return 1;
            case EARTH:
                return 2;
            case MARS:
                return 3;
            case JUPITER:
                return 4;
            case SATURN:
                return 5;
            case URANUS:
                return 6;
            case NEPTUNE:
                return 7;
            case PLUTO:
                return 8;
            case MOON:
                return 9;
            case IO:
                return 10;
            default:
                return 0;
        }
    }

    // we only have to chage this if we are going to change the cosmic ray service available planets
    public static PostOfficeLocation getRandomCosmicRayServiceStation() {
        // select the planet randomly
        double rand = Math.random();
        if (rand > 0.5) {

            return PostOfficeLocation.JUPITER;
        } else {

            return PostOfficeLocation.NEPTUNE;
        }
    }
}
