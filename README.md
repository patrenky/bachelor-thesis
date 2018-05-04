## Mobile application for acquisition and editing HDR images

<div style="text-align:center">
    <img src ="./app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" width="100" />
</div>

Mobile application that solves HDR image processing problems and focuses to interactivity with the user, offering him more opportunities in clear and minimalistic graphic user interface.
Application generate HDR content by merging a series of low dynamic range images with different exposure time values. The solution provides work with HDR photography, four tone mapping methods and various tools for user.

##### digital photography --- image processing --- mobile application --- high dynamic range --- tone mapping

#### Import project to Android Studio IDE

- [Import sources](https://github.com/dogriffiths/HeadFirstAndroid/wiki/How-to-open-a-project-in-Android-Studio)
- [Import OpenCV](https://opencv.org/platforms/android/)

#### Creating HDR content

Application implements method
[Debevec, P.; Malik, J.: Recovering High Dynamic Range Radiance Maps from Photographs](http://www.pauldebevec.com/Research/HDR/debevec-siggraph97.pdf)

#### Future development TODOs

- Multiplatform support, idea:
    - native image processing: C++ (OpenCV)
    - 2 modules to control device cameras (Android/iOS)
- save and load device CRF to/from local storage
- separation app to 2 activities (to save RAM memory):
    - capturing scenes
    - generating and editing HDR content
- better TMOs + filters

**Author:** Patrik Michalák (xmicha65)

**Supervisor:** Doc. Ing. Martin Čadík, Ph.D.
