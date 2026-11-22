interface Button { void renderButton(); }
interface CheckBox {void renderCheckbox(); }
interface TextField { void renderText(); }


class DarkButton implements Button {
    public void renderButton() { System.out.println("Rendering Dark Button"); }
}
class LightButton implements Button {
        public void renderButton() { System.out.println("Rendering Light Button"); }
}
class LightCheckBox implements CheckBox {
    public void renderCheckbox() {
        System.out.println("Rendering LIGHT checkbox...");
    }
}
class DarkCheckBox implements CheckBox {
    public void renderCheckbox() {
        System.out.println("Rendering DARK checkbox...");
    }
}
class LightTextField implements TextField {
    @Override
    public void renderText() {
        System.out.println("Rendering light text...");
    }
}
class DarkTextField implements TextField {
    @Override
    public void renderText() {
        System.out.println("Rendering dark text...");
    }
}

// TODO: Task 1
interface UIFactory {
    Button createButton();
    CheckBox createCheckbox();
    TextField createTextField();
}

class LightUIFactory implements UIFactory {
    @Override
    public Button createButton() {
        return new LightButton();
    }

    @Override
    public CheckBox createCheckbox() {
        return new LightCheckBox();
    }

    @Override
    public TextField createTextField() {
        return new LightTextField();
    } 
}
class DarkUIFactory implements UIFactory {
    @Override
    public Button createButton() {
        return new DarkButton();
    }

    @Override
    public CheckBox createCheckbox() {
        return new DarkCheckBox();
    }

    @Override
    public TextField createTextField() {
        return new DarkTextField();
    } 
}


// TODO: Task 2
class LoginScreen {
    private Button button;
    private CheckBox checkBox;
    private TextField textField;

    public LoginScreen(UIFactory theme) {
        button = theme.createButton();
        checkBox = theme.createCheckbox();
        textField = theme.createTextField();
    }

    public void setTheme(UIFactory theme) {
        button = theme.createButton();
        checkBox = theme.createCheckbox();
        textField = theme.createTextField();
    }

    public void renderLoginScreen() {
        button.renderButton();
        checkBox.renderCheckbox();
        textField.renderText();
    }
}

// TODO: Task 3
/**
 * it prevents us to use classes of different theme. for a given theme, it enforces us to use the button, checkbox, textfield of all same type based on configuration 
 * defined in ThemeFactory (light+dark).
 */

public class Code_E4 {
    public static void main(String[] args) {
        LoginScreen screen = new LoginScreen(new DarkUIFactory());
        screen.renderLoginScreen();

        screen.setTheme(new LightUIFactory());
        screen.renderLoginScreen();
    }
}
