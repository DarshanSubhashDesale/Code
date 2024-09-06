
SUMMARY = "TR69 agent Headers"
SECTION = "console/utils"

LICENSE = "commercial-gSOAP & gSOAP-1.3b & Comcast & Dimark & MIT & FSF-Unlimited"
LIC_FILES_CHKSUM = "file://LICENSES.TXT;md5=cbbc1e700af12508df6d6ee261c46176"

PV = "${RDK_RELEASE}+git${SRCPV}"

SRC_URI = "${CMF_GIT_ROOT}/components/generic/tr69;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS = "iarmbus iarmmgrs e2fsprogs"

do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}/rdk/tr69agent
    install -m 0644 ${S}/src/integration_interfaces/*.h ${D}${includedir}/rdk/tr69agent
}

ALLOW_EMPTY_${PN} = "1"
