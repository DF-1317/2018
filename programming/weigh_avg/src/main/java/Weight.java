
/*
 * This code demonstrates weighted averaging.
 *
 * The algorithm is this: use 90% of the values we have already collected, and 10% of the
 * new value to determine the average so far. No need to remember the total sum, nor is
 * there a need to remember the number of data points.
 *
 * Why do this? When reading values from a sensor, like an untra-sonic distance detector,
 * we sometimes get spurious values. Rather than take them at face values, we use them
 * in calculating a weighted average. This lessens the impact of a bad reading; the average
 * only moves slightly.
 *
 * If the values [distance] is really changing to the plus or minus end of the number axis,
 * the average will eventually catch up. If you need it to catch up quicker, change the
 * 90/10 weights to something different...like 80/20.
 */
public class Weight {
    static double weightedSum;

    static double avg( double val ) {
        weightedSum = (weightedSum * 0.9) + (val * 0.1);
        return weightedSum;
    }
    /*
     * Feed a bunch of data to the average method. Every now and then throw in
     * a quirky value.
     */
    public static void main(String[] args) {
        System.out.println("Starting weight avg test...");

        double[] vals = {16.0, 15.0, 17.0, 19.0, 18.0, 19.4, 20.2, 11.1, 18.4
                , 19.0, 17.9, 11.2, 18.8, 19.1, 20.22, 11.3, 19.4, 18.0, 17.7
                , 19.0, 17.9, 11.2, 18.8, 19.1, 20.22, 11.3, 19.4, 18.0, 17.7
                , 19.0, 17.9, 11.2, 18.8, 19.1, 20.22, 11.3, 19.4, 18.0, 17.7
                , 19.0, 17.9, 11.2, 18.8, 19.1, 20.22, 11.3, 19.4, 18.0, 17.7
                , 15.0, 14.0, 15.5, 14.6, 13.2, 14.5,  13.6, 14.7, 14.5, 14.0
                , 13.0, 13.5, 15.3, 14.1, 13.0, 11.0,  13.4, 11.2, 13.0, 12.8
                , 12.0, 13.1, 11.8, 11.5, 12.4, 12.8,  10.8, 11.8, 11.3, 12.0
                , 13.0, 13.5, 15.3, 14.1, 13.0, 11.0,  13.4, 11.2, 13.0, 12.8
                , 12.0, 13.1, 11.8, 11.5, 12.4, 12.8,  20.8, 11.8, 11.3, 12.0
        };
        weightedSum = 0;

        for (double v : vals ) {
            System.out.println("v: " + v + ", avg: " + avg(v));
        }

        System.out.println("Weight done!");
    }

}
