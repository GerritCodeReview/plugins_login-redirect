load("//tools/bzl:plugin.bzl", "gerrit_plugin")

gerrit_plugin(
    name = "login-redirect",
    srcs = glob(["src/main/java/**/*.java"]),
    manifest_entries = [
        "Gerrit-PluginName: login-redirect",
        "Gerrit-HttpModule: com.googlesource.gerrit.plugins.loginredirect.LoginRedirectModule",
        "Implementation-Title: Login Redirect plug-in",
    ],
    resources = glob(["src/main/resources/**/*"]),
)
