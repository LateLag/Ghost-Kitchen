package ghostkitchen;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.scene.Menu;
import ghostkitchen.scene.Scene;

/**
 * Modelo de projeto básico da JSGE.
 * 
 * JSGE basic project template.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Main extends EngineFrame {
    private static boolean debug = false;

    private Image icon;
    private Scene scene;
    
    public Main() {
        
        super(
            1536,                 // largura                      / width
            864,                 // algura                       / height
            "Window Title",      // título                       / title
            60,                  // quadros por segundo desejado / target FPS
            true,                // suavização                   / antialiasing
            false,               // redimensionável              / resizable
            false,               // tela cheia                   / full screen
            false,               // sem decoração                / undecorated
            false,               // sempre no topo               / always on top
            false                // fundo invisível              / invisible background
        );
    }
    
    /**
     * Cria o mundo do jogo.
     * Esse método executa apenas uma vez durante a inicialização da engine.
     * 
     * Creates the game world.
     * This method runs just one time during engine initialization.
     */
    @Override
    public void create() {
        icon = ImageUtils.loadImage("resources/images/icon.png");
        setWindowIcon( icon );
        setScene(new Menu(this));
    }

    /**
     * Lê a entrada do usuário e atualiza o mundo do jogo.
     * Os métodos de entrada devem ser usados aqui.
     * Atenção: Você NÃO DEVE usar nenhum dos métodos de desenho da engine aqui.
     * 
     * Reads user input and update game world.
     * Input methods should be used here.
     * Warning: You MUST NOT use any of the engine drawing methods here.
     * 
     * @param delta O tempo passado, em segundos, de um quadro para o outro.
     * Time passed, in seconds, between frames.
     */
    @Override
    public void update( double delta ) {
        scene.update(delta);
    }
    
    /**
     * Desenha o mundo do jogo.
     * Todas as operações de desenho DEVEM ser feitas aqui.
     * 
     * Draws the game world.
     * All drawing related operations MUST be performed here.
     */
    @Override
    public void draw() {
        scene.draw();
    }
    
    /**
     * Instancia a engine e a inicia.
     * 
     * Instantiates the engine and starts it.
     */
    public static void main( String[] args ) {
        if(hasFlag(args, "d", "debug")) debug = true;

        new Main();
    }

    private static boolean hasFlag(String[] args, String shortVersion, String longVersion) {
        if(args == null || shortVersion == null && longVersion == null) return false;

        for(String s : args) {
            if(s.equals("-" + shortVersion) || s.equals("--" + longVersion)) return true;
        }

        return false;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        this.scene.setup();
    }

    public static boolean debug() { return debug; }
}
