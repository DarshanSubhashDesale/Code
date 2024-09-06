SUMMARY = "This recipe compiles DVR code base. This has interface classes that would be necessary for all the media players."
SECTION = "console/utils"

LICENSE = "RDK"
LIC_FILES_CHKSUM = "file://${COREBASE}/../meta-rdk-restricted/licenses/RDK;md5=ba986f8eaa991d86ab2ab6f837701a5f"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRCREV = "${AUTOREV}"

SRC_URI = "${CMF_GIT_ROOT}/components/generic/dvr;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=dvrmgr"

S = "${WORKDIR}/git"

do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/dvr/common/dvrmgr/*.h ${D}${includedir}
    install -m 0644 ${S}/dvr/common/sharedtsb/*.h ${D}${includedir}
    install -m 0644 ${S}/dvr/common/tasseograph/*.h ${D}${includedir}
    install -m 0644 ${S}/dvr/sink/*.h ${D}${includedir}
    install -m 0644 ${S}/dvr/source/*.h ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"
