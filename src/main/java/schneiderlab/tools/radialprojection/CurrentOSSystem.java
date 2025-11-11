package schneiderlab.tools.radialprojection;

public enum CurrentOSSystem {
    WINDOWS,
    MAC,
    LINUX,
    OTHER;

    public static CurrentOSSystem getCurrent() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return WINDOWS;
        if (os.contains("mac")) return MAC;
        if (os.contains("nux") || os.contains("nix")) return LINUX;
        return OTHER;
    }
}
