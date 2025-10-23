package tela_main_controller;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.control.Label;
import java.util.Locale;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 🚀 MainLayoutController ----------------------------------------- Este controller
 * gerencia o menu lateral do sistema. Ele troca as telas exibidas no painel
 * central dinamicamente, permitindo a navegação entre Dashboard, Cadastro de
 * Colmeia, etc.
 */

public class MainLayoutController {

	@FXML
	private Label labelRelogio;

	@FXML
	private Label labelEstacao;

	// StackPane é um painel onde as telas são "empilhadas".
	// Aqui, ele funciona como "conteúdo principal" da aplicação.
	@FXML
	private StackPane painelConteudo;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", Locale.forLanguageTag("pt-BR"));


	// Esse método é chamado automaticamente quando o FXML é carregado
	@FXML
	public void initialize() {
		iniciarRelogio();
		abrirDashboard(); // Abre o dashboard logo no início

	}

	private void iniciarRelogio() {
	    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), _ -> {
	        LocalDateTime agora = LocalDateTime.now();
	        String textoFormatado = formatter.format(agora); // ex: "11 agosto, 15:40"

	        // Capitalizar a primeira letra do mês
	        String textoComMesMaiusculo = capitalizarMes(textoFormatado);

	        labelRelogio.setText(textoComMesMaiusculo);
	        labelEstacao.setText(obterEstacao(agora.getMonthValue()));
	    }), new KeyFrame(Duration.seconds(60)));
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	}

	private String capitalizarMes(String texto) {
		if (texto == null || texto.isEmpty())
			return texto;

		// Exemplo de entrada: "11 agosto, 15:40"
		// Queremos: "11 Agosto, 15:40"
		String[] partes = texto.split(" ");
		if (partes.length < 2)
			return texto;

		// Capitaliza a primeira letra do mês (segunda parte, removendo vírgula)
		String mes = partes[1].replace(",", "");
		mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);

		// Reconstroi o texto, mantendo o resto igual
		// partes[0] = dia, partes[1] = mês (sem vírgula)
		// o restante da string (a partir da vírgula) vai concatenado
		int indexVirgula = texto.indexOf(",");
		String resto = indexVirgula != -1 ? texto.substring(indexVirgula) : "";

		return partes[0] + " " + mes + resto;
	}

	private String obterEstacao(int mes) {
	    if (mes == 12 || mes <= 2) {
	        return "Verão - Flores e abelhas ativas, colhendo mel.";
	    } else if (mes >= 3 && mes <= 5) {
	        return "Outono - Colmeias se preparam para descanso.";
	    } else if (mes >= 6 && mes <= 8) {
	        return "Inverno - Silêncio e proteção para as abelhas.";
	    } else if (mes >= 9 && mes <= 11) {
	        return "Primavera - Florescem as plantas e abelhas despertam.";
	    }
	    return "";
	}

	/**
	 * Método chamado ao clicar no botão "Dashboard". Ele carrega a tela
	 * TelaDashboard.fxml dentro do painel.
	 */
	public void abrirDashboard() {
		carregarTela("/telas/view/TelaDashboard.fxml");
	}

	/**
	 * Método chamado ao clicar no botão "Colmeias". Ele carrega a tela de cadastro
	 * de colmeia.
	 */
	public void abrirListaColmeia() {
	    carregarTela("/telas/view/TelaListaColmeia.fxml");
	}


	/**
	 * Este método centraliza o carregamento de qualquer FXML no painel de conteúdo
	 * (StackPane). Ele substitui a tela atual.
	 *
	 * @param caminho Caminho do arquivo FXML a ser carregado
	 */
	private void carregarTela(String caminho) {
		try {
			// Carrega o FXML especificado
			Node tela = FXMLLoader.load(getClass().getResource(caminho));

			tela.setOpacity(0); // invisível no início somente efeito da tela estilos

			// Substitui o conteúdo do painel pela nova tela
			painelConteudo.getChildren().setAll(tela);

			// Animação fade-in
			FadeTransition fade = new FadeTransition(Duration.millis(900), tela);
			fade.setFromValue(0);
			fade.setToValue(1);
			fade.play();

		} catch (IOException e) {
			e.printStackTrace(); // Mostra erro no console
		}
	}

	/**
	 * Método chamado ao clicar no botão "Sair". Encerra a aplicação com segurança.
	 */
	@FXML
	private void sair() {
		Platform.exit();
	}
}
