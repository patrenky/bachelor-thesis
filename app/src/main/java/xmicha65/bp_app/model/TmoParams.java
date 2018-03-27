package xmicha65.bp_app.model;

/**
 * Enum of default controls values of tonemappers
 */
public enum TmoParams {
    gama,           // range [1, 3] default 2.2
    saturation,     // range [0, 4] default 1
    // Mantiuk
    mScale,         // range [0.6, 0.9] default 0.7
    // Reinhard
    rIntensity,     // range [-8, 8] default 0
    rLightAdapt,    // range [0, 1] default 0
    rColorAdapt,    // range [0, 1] default 0
    // Durand
    dContrast,      // range [0, 8] default 4
    dSigmaSpace,    // range [0, 4] default 2
    dSigmaColor,    // range [0, 4] default 2
    // Drago
    dBias;          // range [0, 1] default 0.85

    /**
     * @return default value of tonemapper from param range
     */
    public static float getDefaultValue(TmoParams param) {
        switch (param) {
            case gama:
                return 2.2f;
            case saturation:
                return 1.0f;
            case mScale:
                return 0.7f;
            case dContrast:
                return 4.0f;
            case dSigmaSpace:
            case dSigmaColor:
                return 2.0f;
            case dBias:
                return 0.85f;
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
            case gama:
                return 60;
            case saturation:
                return 25;
            case rIntensity:
            case dContrast:
            case dSigmaSpace:
            case dSigmaColor:
                return 50;
            case mScale:
                return 30;
            case dBias:
                return 85;
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
            case gama:
                return (float) progress / 50 + 1;
            case mScale:
                return (float) progress / 300 + 0.6f;
            case rIntensity:
                return (float) (progress - 50) / (50 / 8);
            case dContrast:
                return (float) progress / 12.5f;
            case saturation:
            case dSigmaSpace:
            case dSigmaColor:
                return (float) progress / 25;
            case rLightAdapt:
            case rColorAdapt:
            case dBias:
            default:
                return (float) progress / 100;
        }
    }
}
