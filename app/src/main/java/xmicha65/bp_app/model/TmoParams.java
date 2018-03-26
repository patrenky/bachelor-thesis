package xmicha65.bp_app.model;

/**
 * Enum of default controls values of tonemappers
 */
public enum TmoParams {
    // Reinhard
    rGama,          // range [1, 3]
    rIntensity,     // range [-8, 8]
    rLightAdapt,    // range [0, 1]
    rColorAdapt;    // range [0, 1]

    /**
     * @return default value of tonemapper from param range
     */
    public static float getDefaultValue(TmoParams param) {
        switch (param) {
            case rGama:
                return 2.2f;
            case rIntensity:
            case rLightAdapt:
            case rColorAdapt:
            default:
                return 0.0f;
        }
    }

    /**
     * @return default pogress bar value from 0-100 range
     */
    public static int getDefaultProgressValue(TmoParams param) {
        switch (param) {
            case rGama:
                return 60;
            case rIntensity:
                return 50;
            case rLightAdapt:
            case rColorAdapt:
            default:
                return 0;
        }
    }

    /**
     * @return counted value of progress bar in case of param range
     */
    public static float getProgressValue(TmoParams param, int progress) {
        switch (param) {
            case rGama:
                return (float) progress / 50 + 1;
            case rIntensity:
                return (float) (progress - 50) / (50 / 8);
            case rLightAdapt:
            case rColorAdapt:
            default:
                return (float) progress / 100;
        }
    }
}
