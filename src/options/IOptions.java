package options;

public interface IOptions {
    void parseOptions(String[] args);

    void showHelp();

    void showBanner();
}
