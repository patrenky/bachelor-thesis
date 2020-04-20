package xmicha65.bp_app.model;

/**
 * Enum of default controls values of tonemappers
 */
public enum TmoParams {
    gamma,          // range [1, 3] default 1.5
    saturation,     // range [0, 4] default 1
    // Mantiuk
    mScale,         // range [0.6, 0.9] default 0.6
    // Reinhard
    rIntensity,     // range [-8, 8] default 0
    rLightAdapt,    // range [0, 1] default 0.75
    rColorAdapt,    // range [0, 1] default 0
    // Durand
    duGamma,        // range [1, 3] default 1.2
    duContrast,     // range [0, 8] default 1
    duSigmaSpace,   // range [0, 4] default 4
    duSigmaColor,   // range [0, 4] default 4
    // Drago
    drBias;         // range [0, 1] default 0.7

    /**
     * @return default value of tonemapper from param range
     */
    public static float getDefaultValue(TmoParams param) {
        switch (param) {
            case duSigmaSpace:
            case duSigmaColor:
                return 4.0f;
            case gamma:
                return 1.5f;
            case duGamma:
                return 1.2f;
            case saturation:
            case duContrast:
                return 1.0f;
            case rLightAdapt:
                return 0.75f;
            case drBias:
                return 0.7f;
            case mScale:
                return 0.6f;
            case rIntensity:
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
            case duSigmaSpace:
            case duSigmaColor:
                return 100;
            case rLightAdapt:
                return 75;
            case drBias:
                return 70;
            case rIntensity:
                return 50;
            case gamma:
            case saturation:
                return 25;
            case duGamma:
                return 10;
            case duContrast:
                return 13;
            case mScale:
                return 0;
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
            case gamma:
            case duGamma:
                return (float) progress / 50 + 1;
            case mScale:
                return (float) progress / 300 + 0.6f;
            case rIntensity:
                return (float) (progress - 50) / (50 / 8);
            case duContrast:
                return (float) progress / 12.5f;
            case saturation:
            case duSigmaSpace:
            case duSigmaColor:
                return (float) progress / 25;
            case rLightAdapt:
            case rColorAdapt:
            case drBias:
            default:
                return (float) progress / 100;
        }
    }
}
