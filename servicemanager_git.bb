SUMMARY = "Service Manager"
LICENSE = "RDK & MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://../../LICENSES.TXT;md5=250308243d14105a41f4e37a87ac1b8a"

PV = "${RDK_RELEASE}"

SECTION = "qtapps"
DEPENDS = "qtbase devicesettings rmfstreamer storagemanager qtwebsockets pxcore-libnode tts virtual/secapi wpeframework"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'webkit', 'qtwebkit', '', d)}"
DEPENDS += "gst-plugins-rdk-aamp"

EXTRA_QMAKEVARS_PRE += "${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm', 'DEFINES+=USE_UNIFIED_CONTROL_MGR_API_1', 'DEFINES+=BASTILLE_37', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm', 'ctrlm-headers', '', d)}"

EXTRA_QMAKEVARS_PRE += "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'DEFINES+=ENABLE_BLUETOOTH_CONTROL', '', d)}"
EXTRA_QMAKEVARS_PRE += "DEFINES+=TRM_USE_SSL"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetooth-mgr', '', d)}"

RDEPENDS_${PN} += "rdkservices"

DEPENDS += "safec-common-wrapper"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', ' ', d)}"
inherit pkgconfig
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CXXFLAGS_append_client = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"

LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CXXFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"

EXTRA_QMAKEVARS_PRE += "CONFIG+=build_refactored_storagemanager"
EXTRA_QMAKEVARS_PRE += "CONFIG+=build_with_pxscene"

EXTRA_QMAKEVARS_PRE += "DEFINES+=USE_YAJL2"

CXXFLAGS += "-std=c++11"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/servicemanager;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=servicemanager"
SRC_URI += "${CMF_GIT_ROOT}/rdk/devices/intel-x86-pc/emulator/servicemanager;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};destsuffix=git/platform/rdkemulator;name=svcmgrplat"
SRCREV_FORMAT = "${AUTOREV}"
SRCREV_servicemanager = "${AUTOREV}"
SRCREV_svcmgrplat = "${AUTOREV}"

CXXFLAGS += "-I${STAGING_INCDIR}/pxcore -I${STAGING_INCDIR}/freetype2"
CXXFLAGS += "-I${STAGING_INCDIR}/libnode -I${STAGING_INCDIR}/libnode/deps/uv -I${STAGING_INCDIR}/libnode/deps/v8 -I${STAGING_INCDIR}/libnode/deps/cares"
CXXFLAGS += "-I${STAGING_INCDIR}/libnode/deps/debugger-agent/include -I${STAGING_INCDIR}/libnode/deps/v8/include"
CXXFLAGS += "-std=c++11"

S = "${WORKDIR}/git/build/servicemanager"
QMAKE_PROFILES = "${S}/../servicemanagermain.pro"
RPROVIDES_${PN} += "libservicemanager1"
RDEPENDS_${PN} += "devicesettings tts"

inherit qmake5 coverity
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"
CXXFLAGS += "-I${STAGING_INCDIR}/pxcore"

FILES_${PN} += "/home/root"
FILES_${PN}-dbg += "/home/root/.debug"
FILES_${PN} += "/lib/rdk"
FILES_${PN}-dbg += "/lib/rdk/.debug"

do_install() {
    install -d ${D}${includedir}/rdk/servicemanager
    install -d ${D}${libdir}
    install -d ${D}${sysconfdir} ${D}${sysconfdir}/rfcdefaults

    cp -dr ${S}/../../include/* ${D}${includedir}/rdk/servicemanager
    cp -dr ${B}/servicemanager/*.so* ${D}${libdir}
    cp -dr ${B}/soundplayer/*.so* ${D}${libdir}
    install -d ${D}${bindir}
    install -m 0755 ${B}/serviceMgrUnitTest/serviceMgrUnitTest ${D}${bindir}/
    install -m 0644 ${S}/../../build/servicemanager/service_manager_settings.conf ${D}${sysconfdir}

    cp -dr ${S}/../../include/services/applicationmanagerservice.h ${D}${includedir}/rdk/servicemanager/
    install -d ${D}/home/root
    cp -dr ${B}/servicemanagerapp/servicemanagerapp ${D}/home/root

    install -d ${D}/lib/rdk
    install -m 0755 ${S}/../../build/servicemanagerapp/runServiceManager.sh ${D}/lib/rdk/
    install -m 0755 ${S}/../../rfcdefaults/servicemanager.ini ${D}${sysconfdir}/rfcdefaults
}

export QTROOT = '='
export FSROOT = '='
export RDK_FSROOT_PATH = "${STAGING_DIR_TARGET}"
FILES_${PN} += "${sysconfdir}/rfcdefaults/servicemanager.ini"
