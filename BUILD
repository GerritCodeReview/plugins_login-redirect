load("@com_googlesource_gerrit_bazlets//:gerrit_plugin.bzl", "gerrit_plugin")

gerrit_plugin(
    srcs = glob(["src/main/java/**/*.java"]),
    manifest_entries = [
        "Gerrit-PluginName: login-redirect",
        "Gerrit-HttpModule: com.googlesource.gerrit.plugins.loginredirect.LoginRedirectModule",
        "Implementation-Title: Login Redirect plug-in",
    ],
    plugin = "login-redirect",
    resources = glob(["src/main/resources/**/*"]),
)
