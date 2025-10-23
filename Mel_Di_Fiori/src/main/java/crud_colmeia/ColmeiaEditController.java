package crud_colmeia;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import model.Colmeia;

import java.time.LocalDate;

public class ColmeiaEditController {
    @FXML private TextField txtNumero;
    @FXML private DatePicker dateInstalacao;
    @FXML private TextField txtLocalizacao;
    @FXML private ComboBox<String> comboTipo;
    @FXML private ComboBox<String> comboSituacao;
    @FXML private Spinner<Integer> spinnerNumeroQuadros;
    @FXML private TextArea txtObservacoes;
    
    private Colmeia colmeiaEmEdicao;
    
    /**
     * Método chamado ao inicializar a tela
     */
    @FXML
    public void initialize() {
        // Configura as opções dos ComboBox
        comboSituacao.getItems().addAll("Cirurgia", "Medicação", "Vacinação");
        comboTipo.getItems().addAll("Cachorro", "Gato", "Aves", "Outros");
        
        // Configura o spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 10);
        spinnerNumeroQuadros.setValueFactory(valueFactory);
    }
    
    /**
     * Carrega os dados da colmeia selecionada para edição
     */
    public void carregarColmeia(Colmeia colmeia) {
        this.colmeiaEmEdicao = colmeia;
        carregarDados();
    }
    
    /**
     * Preenche os campos com os dados da colmeia
     */
    private void carregarDados() {
        if (colmeiaEmEdicao != null) {
            txtNumero.setText(colmeiaEmEdicao.getIdentificacao());
            dateInstalacao.setValue(colmeiaEmEdicao.getDataInstalacao());
            txtLocalizacao.setText(colmeiaEmEdicao.getLocalizacao());
            comboTipo.setValue(colmeiaEmEdicao.getTipo());
            comboSituacao.setValue(colmeiaEmEdicao.getStatus());
            spinnerNumeroQuadros.getValueFactory().setValue(colmeiaEmEdicao.getNumeroQuadros());
            txtObservacoes.setText(colmeiaEmEdicao.getObservacoes());
        }
    }
    
    /**
     * Valida se todos os campos obrigatórios estão preenchidos
     */
    private boolean validarCampos() {
        boolean valido = true;
        limparEstiloErro();
        
        if (txtNumero.getText() == null || txtNumero.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtNumero);
            valido = false;
        }
        
        if (txtLocalizacao.getText() == null || txtLocalizacao.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtLocalizacao);
            valido = false;
        }
        
        if (comboTipo.getValue() == null || comboTipo.getValue().trim().isEmpty()) {
            colocarBordaVermelha(comboTipo);
            valido = false;
        }
        
        if (comboSituacao.getValue() == null || comboSituacao.getValue().trim().isEmpty()) {
            colocarBordaVermelha(comboSituacao);
            valido = false;
        }
        
        if (dateInstalacao.getValue() == null) {
            colocarBordaVermelha(dateInstalacao);
            valido = false;
        }
        
        return valido;
    }
    
    private void colocarBordaVermelha(Control campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }
    
    private void limparBordaVermelha(Control campo) {
        campo.setStyle("");
    }
    
    private void limparEstiloErro() {
        limparBordaVermelha(txtNumero);
        limparBordaVermelha(txtLocalizacao);
        limparBordaVermelha(comboTipo);
        limparBordaVermelha(comboSituacao);
        limparBordaVermelha(dateInstalacao);
    }
    
    /**
     * Salva as alterações da colmeia
     */
    @FXML
    private void salvarEdicao() {
        try {
            if (!validarCampos()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos obrigatórios");
                alerta.setHeaderText("Preencha os campos em vermelho");
                alerta.setContentText("Os campos com borda vermelha são obrigatórios e não podem ficar vazios.");
                alerta.showAndWait();
                return;
            }
            
            // Atualiza os dados da colmeia
            colmeiaEmEdicao.setIdentificacao(txtNumero.getText());
            colmeiaEmEdicao.setDataInstalacao(dateInstalacao.getValue());
            colmeiaEmEdicao.setLocalizacao(txtLocalizacao.getText());
            colmeiaEmEdicao.setTipo(comboTipo.getValue());
            colmeiaEmEdicao.setStatus(comboSituacao.getValue());
            colmeiaEmEdicao.setNumeroQuadros(spinnerNumeroQuadros.getValue());
            colmeiaEmEdicao.setObservacoes(txtObservacoes.getText());
            
            // Salva no banco
            new DAO<>(Colmeia.class).atualizarTransacional(colmeiaEmEdicao);
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Edição Concluída");
            alerta.setHeaderText("Sucesso");
            alerta.setContentText("Animal editado com sucesso!");
            alerta.showAndWait();
            
            // Volta para a tela de lista
            voltarParaLista();
            
        } catch (Exception e) {
            e.printStackTrace();
            
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Falha ao editar animal");
            alerta.setContentText("Erro: " + e.getMessage());
            alerta.showAndWait();
        }
    }
    
    /**
     * Cancela a edição e volta para a lista
     */
    @FXML
    private void cancelarEdicao() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Cancelamento");
        confirmacao.setHeaderText("Deseja cancelar a edição?");
        confirmacao.setContentText("As alterações não salvas serão perdidas.");
        
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                voltarParaLista();
            }
        });
    }
    
    /**
     * Volta para a tela de lista de colmeias
     */
    private void voltarParaLista() {
        try {
            StackPane painel = (StackPane) txtNumero.getScene().lookup("#painelConteudo");
            if (painel != null) {
                javafx.scene.Node telaLista = javafx.fxml.FXMLLoader.load(
                    getClass().getResource("/telas/view/TelaListaColmeia.fxml")
                );
                painel.getChildren().setAll(telaLista);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}