interface Title { void renderTitle(); }
interface Menu { void renderMenu(); }
interface Background { void renderBackground(); }

class LinuxTitle implements Title {
    @Override
    public void renderTitle() {
        System.out.println("Linux title rendering...");
    }
}
class WindowsTitle implements Title {
    @Override
    public void renderTitle() {
        System.out.println("Windows title rendering...");
    }
}
class MacTitle implements Title {
    @Override
    public void renderTitle() {
        System.out.println("MacOS title rendering...");
    }
}

class LinuxMenu implements Menu {

    @Override
    public void renderMenu() {
        System.out.println("Linux menu rendering...");
    }
}
class WindowsMenu implements Menu {

    @Override
    public void renderMenu() {
        System.out.println("Windows menu rendering...");
    }
}
class MacOSMenu implements Menu {

    @Override
    public void renderMenu() {
        System.out.println("MacOS menu rendering...");
    }
}

class LinuxBackground implements Background {
    @Override
    public void renderBackground() {
        System.out.println("Linux Background rendering...");
        
    }
}
class WindowsBackground implements Background {
    @Override
    public void renderBackground() {
        System.out.println("Windows Background rendering...");
        
    }
}
class MacOSBackground implements Background {
    @Override
    public void renderBackground() {
        System.out.println("MacOS Background rendering...");
        
    }
}


interface ThemeFactory {
    Title createTitle();
    Menu createMenu();
    Background createBackground();
}

class LinuxTheme implements ThemeFactory {

    @Override
    public Background createBackground() {
        return new LinuxBackground();
    }

    @Override
    public Menu createMenu() {
        return new LinuxMenu();
    }

    @Override
    public Title createTitle() {
        return new LinuxTitle();
    }
}

class WindowsTheme implements ThemeFactory {

    @Override
    public Background createBackground() {
        return new WindowsBackground();
    }

    @Override
    public Menu createMenu() {
        return new WindowsMenu();
    }

    @Override
    public Title createTitle() {
        return new WindowsTitle();
    }
}


class Application {
    private Background background;
    private Title title;
    private Menu menu;

    public Application(ThemeFactory theme) {
        this.title = theme.createTitle();
        this.menu = theme.createMenu();
        this.background = theme.createBackground();
    }

    public void renderOS() {
        background.renderBackground();
        title.renderTitle();
        menu.renderMenu();
    }

    public void setTheme(ThemeFactory theme) {
        this.title = theme.createTitle();
        this.menu = theme.createMenu();
        this.background = theme.createBackground();
    }
}

public class _4_AbstractFactory {
    public static void main(String[] args) {
        ThemeFactory factory = new LinuxTheme();
        Application app = new Application(factory);
        app.renderOS();

        app.setTheme(new WindowsTheme());
        app.renderOS();
    }
}
