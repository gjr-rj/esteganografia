/*******************************************************************************
*** Descrição: Systema proposto no Trabalho de Conclusão de Curso - UFF - 2015
***            Esteganografia em Mídias Digitais
***
*** Curso:     Tecnologia em Sistemas de COmputação
*** @versão:   1.0 de 02/10/2015
*** @Autor:    Geraldo José Ferreira Chagas Júnior
*******************************************************************************/

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;

import java.io.File;

public class EstegMidiaDig extends JFrame
{
  /*****************************************************************************
  *** Classes propostas pelo TCC
  *****************************************************************************/
  private TArquivo arqMidia;
  private TArquivo arqOcultar;
  private TFinalArquivo estegFinalArq;
  private TLSB estegLsb;

  /*******************************************************
  **  Objetos da Janela principal
  ********************************************************/
  private JTabbedPane painelPrincipal = new JTabbedPane();
  private JPanel painelOcultar = new JPanel(null, true);
  private JPanel painelGrupoDado = new JPanel(null, true);
  private JLabel jlblSelArq = new JLabel();
  private JTextField jtxtArq = new JTextField();
  private JButton Selecionar = new JButton();
  private JLabel jlblDigtMsg = new JLabel();
  private JTextArea jtxtaMsg = new JTextArea("");
  private JScrollPane jtxtaMsgScrollPane = new JScrollPane(jtxtaMsg);
  private JLabel jlblOu = new JLabel();
  private JLabel jlblTamArq = new JLabel();
  private JPanel painelGrupoTipoEsteganografia = new JPanel(null, true);
  private JRadioButton jrdbFinalArq = new JRadioButton();
  private JRadioButton jrdbLsb = new JRadioButton();
  private JRadioButton jrdbLsb2 = new JRadioButton();
  private JRadioButton jrdbLsb3 = new JRadioButton();
  private JRadioButton jrdbLsbn = new JRadioButton();
  private JSpinner jSpn = new JSpinner();
  private SpinnerNumberModel jSpnModel = new SpinnerNumberModel (4, 4, 7, 1);
  private JRadioButton jrdbLsbCiclico = new JRadioButton();
  private ButtonGroup jbntGpMetodos = new ButtonGroup();
  private JPanel painelMax = new JPanel(null, true);
  private JLabel jlblLimitArq = new JLabel();
  private JButton jbtnOcultar = new JButton();
  private JPanel painelInfoMdia = new JPanel(null, true);
  private JLabel jlblInfoArqMidia = new JLabel();
  private JLabel jlblInfoArqMidiaEsteg = new JLabel();
  private JTextField jTxtAbrirMidia = new JTextField();
  private JLabel jlblAbrirMidia = new JLabel();
  private JButton jbtnLocalizarMidia = new JButton();
  private JPanel painelRecuperar = new JPanel(null, true);
  private JLabel jlblAbrirArqMidiaEsteg = new JLabel();
  private JTextField jtxtArqMidiaEsteg = new JTextField();
  private JButton jbtnLocalizarMidiaEsteg = new JButton();
  private JPanel painelnfoMidiaEsteg = new JPanel(null, true);
  private JButton jbtnRecuperar = new JButton();
  private JTextArea jtxtaMsgRecuperada = new JTextArea("");
  private JScrollPane jtxtaMsgRecuperadaScrollPane = 
                                            new JScrollPane(jtxtaMsgRecuperada);
  private JLabel jlblMsgEstegano = new JLabel();
  
  //Construtor
  public EstegMidiaDig(String title)
  {
    /************************************************************
    *             Controle do layout
    ************************************************************/
    super(title);
    setIconImage ((new ImageIcon("img/icone.png")).getImage());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 609; 
    int frameHeight = 549;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setResizable(false);
    Container cp = getContentPane();
    cp.setLayout(null);

    jrdbFinalArq.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        verifiOpcEsteganografia();
      }
    });
    jbntGpMetodos.add(jrdbFinalArq);

    jrdbLsb.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        verifiOpcEsteganografia();
      }
    });
    jbntGpMetodos.add(jrdbLsb);

    jrdbLsb2.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        verifiOpcEsteganografia();
      }
    });
    jbntGpMetodos.add(jrdbLsb2);

    jrdbLsb3.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        verifiOpcEsteganografia();
      }
    });
    jbntGpMetodos.add(jrdbLsb3);

    jrdbLsbn.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        jSpn.setEnabled(jrdbLsbn.isSelected());
        verifiOpcEsteganografia();
      }
    });
    jbntGpMetodos.add(jrdbLsbn);

    jrdbLsbCiclico.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        verifiOpcEsteganografia();
      }
    });
    jbntGpMetodos.add(jrdbLsbCiclico);

    painelPrincipal.setBounds(16, 16, 560, 478);
    painelPrincipal.addTab("Ocultar Dados", painelOcultar);
    painelPrincipal.addTab("Recuperar Dados", painelRecuperar);

    painelPrincipal.setSelectedIndex(0);
    cp.add(painelPrincipal);
    jTxtAbrirMidia.setBounds(14, 32, 454, 25);
    jTxtAbrirMidia.setEnabled(false);
    jTxtAbrirMidia.setEditable(true);
    painelOcultar.add(jTxtAbrirMidia);
    jlblAbrirMidia.setBounds(14, 8, 128, 20);
    jlblAbrirMidia.setText("Abrir arquivo de mídia");
    painelOcultar.add(jlblAbrirMidia);
    jbtnLocalizarMidia.setBounds(470, 32, 75, 25);
    jbtnLocalizarMidia.setText("Localizar");
    jbtnLocalizarMidia.setMargin(new Insets(2, 2, 2, 2));

    //Clique no botão localizar
    jbtnLocalizarMidia.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        final JFileChooser FcOpen = new JFileChooser();
        FcOpen.setAcceptAllFileFilterUsed(false);

        //Imagens
        FcOpen.setFileFilter(new FileFilter()
        {
          @Override
          public String getDescription() { return "Arquivos de " +
                           "imagens esteganografáveis (*.bmp, *.jpg, *.jpeg)"; }

          @Override
          public boolean accept(File f)
          {
            if (f.isDirectory()) { return true; }
            else
            {
              String filename = f.getName().toLowerCase();
              return filename.endsWith(".jpg") || 
                     filename.endsWith(".jpeg") || 
                     filename.endsWith(".bmp");
            }
          }
         });

        //Audio
        FcOpen.setFileFilter(new FileFilter()
        {
          @Override
          public String getDescription() { return "Arquivos de áudio " + 
                                           "esteganografáveis (*.wav, *.mp3)"; }

          @Override
          public boolean accept(File f)
          {
            if (f.isDirectory()) { return true; }
            else
            {
              String filename = f.getName().toLowerCase();
              return filename.endsWith(".wav") || filename.endsWith(".mp3");
             }
          }
         });

        //Vídeo
        FcOpen.setFileFilter(new FileFilter()
        {
          @Override
          public String getDescription() { return "Arquivos de vídeo " +
                                                  "esteganografáveis (*.avi)"; }

          @Override
          public boolean accept(File f)
          {
            if (f.isDirectory()) { return true; }
            else
            {
              String filename = f.getName().toLowerCase();
              return filename.endsWith(".avi");
            }
          }
         });

        int returnVal = FcOpen.showOpenDialog(painelPrincipal);

        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
          //O método que fará o processamento do arquivo selecionado
          abrirArquivoMidia_OK(FcOpen.getSelectedFile().getAbsolutePath());
        }
      }
    });
    painelOcultar.add(jbtnLocalizarMidia);

    painelGrupoDado.setBounds(14, 136, 364, 260);
    painelGrupoDado.setOpaque(false);
    painelGrupoDado.setBorder
                      (BorderFactory.createTitledBorder("Dado a ser ocultado"));
    painelOcultar.add(painelGrupoDado);
    painelGrupoTipoEsteganografia.setBounds(382, 136, 169, 260);
    painelGrupoTipoEsteganografia.setOpaque(false);
    painelGrupoTipoEsteganografia.setBorder
                                   (BorderFactory.createTitledBorder("Método"));
    painelOcultar.add(painelGrupoTipoEsteganografia);
    jrdbFinalArq.setBounds(16, 24, 140, 20);
    jrdbFinalArq.setText("Final de Arquivo");
    jrdbFinalArq.setOpaque(false);
    jrdbFinalArq.setSelected(true);
    jrdbFinalArq.setEnabled(false);
    painelGrupoTipoEsteganografia.add(jrdbFinalArq);
    jrdbLsb.setBounds(16, 48, 100, 20);
    jrdbLsb.setText("LSB");
    jrdbLsb.setOpaque(false);
    jrdbLsb.setEnabled(false);
    painelGrupoTipoEsteganografia.add(jrdbLsb);
    jrdbLsb2.setBounds(16, 72, 100, 20);
    jrdbLsb2.setText("LSB 2");
    jrdbLsb2.setOpaque(false);
    jrdbLsb2.setEnabled(false);
    painelGrupoTipoEsteganografia.add(jrdbLsb2);
    jrdbLsb3.setBounds(16, 96, 100, 20);
    jrdbLsb3.setText("LSB 3");
    jrdbLsb3.setOpaque(false);
    jrdbLsb3.setEnabled(false);
    painelGrupoTipoEsteganografia.add(jrdbLsb3);
    jrdbLsbn.setBounds(16, 120, 100, 20);
    jrdbLsbn.setText("LSB n");
    jrdbLsbn.setOpaque(false);
    jrdbLsbn.setEnabled(false);
    painelGrupoTipoEsteganografia.add(jrdbLsbn);
    jSpn.setBounds(80, 120, 44, 24);
    jSpn.setValue(4);
    jSpn.setEnabled(false);
    jSpn.setModel(jSpnModel);    
    painelGrupoTipoEsteganografia.add(jSpn);
    jrdbLsbCiclico.setBounds(16, 144, 100, 20);
    jrdbLsbCiclico.setText("LSB Cíclico");
    jrdbLsbCiclico.setOpaque(false);
    jrdbLsbCiclico.setEnabled(false);
    painelGrupoTipoEsteganografia.add(jrdbLsbCiclico);
    jbtnOcultar.setBounds(382, 408, 169, 33);
    jbtnOcultar.setText("Oculltar");
    jbtnOcultar.setMargin(new Insets(2, 2, 2, 2));
    jbtnOcultar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        jbtnOcultar_OnClick (evt);
      }
    });
    jbtnOcultar.setEnabled(false);

    painelOcultar.add(jbtnOcultar);
    jlblSelArq.setBounds(16, 32, 110, 19);
    jlblSelArq.setText("Selecionar arquivo");
    painelGrupoDado.add(jlblSelArq);
    jtxtArq.setBounds(16, 56, 265, 25);
    jtxtArq.setEditable(true);
    jtxtArq.setEnabled(false);
    painelGrupoDado.add(jtxtArq);
    Selecionar.setBounds(280, 56, 73, 25);
    Selecionar.setText("Localizar");
    Selecionar.setMargin(new Insets(2, 2, 2, 2));
    Selecionar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        JFileChooser FcOpen = new JFileChooser();
        int returnVal = FcOpen.showOpenDialog(painelPrincipal);

        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
          //O método que fará o processamento do arquivo selecionado
          abrirArquivoOcultar_OK(FcOpen.getSelectedFile().getAbsolutePath());
        }
      }
    });
    Selecionar.setEnabled(false);

    painelGrupoDado.add(Selecionar);
    jlblDigtMsg.setBounds(16, 88, 110, 20);
    jlblDigtMsg.setText("Digitar mensagem");
    painelGrupoDado.add(jlblDigtMsg);
    jtxtaMsgScrollPane.setBounds(16, 112, 337, 129);
    jtxtaMsg.setEnabled(false);
    //Eveeto de alteração do texto digitado.
    jtxtaMsg.getDocument().addDocumentListener(new DocumentListener()
    {
      @Override
      public void removeUpdate(DocumentEvent e)  { jtxtaMsg_Alterou(); }

      @Override
      public void insertUpdate(DocumentEvent e) { jtxtaMsg_Alterou(); }

      @Override
      public void changedUpdate(DocumentEvent e)  { jtxtaMsg_Alterou(); }
    });

    painelGrupoDado.add(jtxtaMsgScrollPane);
    jlblOu.setBounds(184, 80, 35, 19);
    jlblOu.setText("ou");
    painelGrupoDado.add(jlblOu);
    painelInfoMdia.setBounds(14, 64, 537, 65);
    painelInfoMdia.setOpaque(false);
    painelInfoMdia.setBorder
            (BorderFactory.createTitledBorder("Inormações do arquvo de Mídia"));
    painelOcultar.add(painelInfoMdia);
    jlblInfoArqMidia.setBounds(8, 24, 515, 33);
    jlblInfoArqMidiaEsteg.setBounds(8, 20, 515, 60);
    jlblInfoArqMidia.setText("Info:");
    jlblInfoArqMidia.setFont(new Font("Dialog", Font.PLAIN, 12));
    painelInfoMdia.add(jlblInfoArqMidia);
    painelnfoMidiaEsteg.add(jlblInfoArqMidiaEsteg);
    painelMax.setBounds(8, 168, 153, 81);
    painelMax.setOpaque(false);
    painelMax.setBorder(BorderFactory.createTitledBorder("Máximo"));
    painelGrupoTipoEsteganografia.add(painelMax);
    jlblLimitArq.setBounds(8, 24, 110, 20);
    jlblLimitArq.setText("Limite:");
    painelMax.add(jlblLimitArq);
    jlblTamArq.setBounds(214, 86, 211, 19);
    jlblTamArq.setText("Tamanho:");
    painelGrupoDado.add(jlblTamArq);
    jlblAbrirArqMidiaEsteg.setBounds(14, 8, 128, 20);
    jlblAbrirArqMidiaEsteg.setText("Abrir arquivo de mídia");
    painelRecuperar.add(jlblAbrirArqMidiaEsteg);
    jtxtArqMidiaEsteg.setBounds(14, 32, 454, 25);
    jtxtArqMidiaEsteg.setEnabled(false);
    painelRecuperar.add(jtxtArqMidiaEsteg);
    jbtnLocalizarMidiaEsteg.setBounds(470, 32, 75, 25);
    jbtnLocalizarMidiaEsteg.setText("Localizar");
    jbtnLocalizarMidiaEsteg.setMargin(new Insets(2, 2, 2, 2));
    jbtnLocalizarMidiaEsteg.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        JFileChooser FcOpen = new JFileChooser();
        FcOpen.setAcceptAllFileFilterUsed(false);

        //Imagens
        FcOpen.setFileFilter(new FileFilter()
        {
          @Override
          public String getDescription() { return "Arquivos de imagens " +
                                   "esteganografáveis (*.bmp, *.jpg, *.jpeg)"; }

          @Override
          public boolean accept(File f)
          {
            if (f.isDirectory()) { return true; }
            else
            {
              String filename = f.getName().toLowerCase();
              return filename.endsWith(".jpg") || 
                     filename.endsWith(".jpeg") || 
                     filename.endsWith(".bmp");
            }
          }
         });

        //Audio
        FcOpen.setFileFilter(new FileFilter()
        {
          @Override
          public String getDescription() { return "Arquivos de áudio " +
                                           "esteganografáveis (*.wav, *.mp3)"; }

          @Override
          public boolean accept(File f)
          {
            if (f.isDirectory()) { return true; }
            else
            {
              String filename = f.getName().toLowerCase();
              return filename.endsWith(".wav") || filename.endsWith(".mp3");
             }
          }
         });

        //Vídeo
        FcOpen.setFileFilter(new FileFilter()
        {
          @Override
          public String getDescription() { return "Arquivos de vídeo " +
                                                  "esteganografáveis (*.avi)"; }

          @Override
          public boolean accept(File f)
          {
            if (f.isDirectory()) { return true; }
            else
            {
              String filename = f.getName().toLowerCase();
              return filename.endsWith(".avi");
            }
          }
         });

        int returnVal = FcOpen.showOpenDialog(painelPrincipal);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
          //O método que fará o processamento do arquivo selecionado
          abrirArquivoMidiaEsteg_OK(FcOpen.getSelectedFile().getAbsolutePath());
        }

      }
    });
    painelRecuperar.add(jbtnLocalizarMidiaEsteg);
    painelnfoMidiaEsteg.setBounds(14, 64, 529, 121);
    painelnfoMidiaEsteg.setOpaque(false);
    painelnfoMidiaEsteg.setBorder
          (BorderFactory.createTitledBorder("Informações do arquivo de Mídia"));
    painelRecuperar.add(painelnfoMidiaEsteg);
    jbtnRecuperar.setBounds(222, 192, 115, 33);
    jbtnRecuperar.setText("Recuperar dado");
    jbtnRecuperar.setMargin(new Insets(2, 2, 2, 2));
    jbtnRecuperar.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        jbtnRecuperar_OnClick (evt);
      }
    });
    jbtnRecuperar.setEnabled(false);
    painelRecuperar.add(jbtnRecuperar);
    jtxtaMsgRecuperadaScrollPane.setBounds(14, 248, 521, 185);
    jtxtaMsgRecuperada.setEnabled(false);
    painelRecuperar.add(jtxtaMsgRecuperadaScrollPane);
    jlblMsgEstegano.setBounds(14, 216, 115, 25);
    jlblMsgEstegano.setText("Mensagem");
    painelRecuperar.add(jlblMsgEstegano);

    setVisible(true);
  }

  public static void main(String[] args)
  {
    new EstegMidiaDig("EstegMidiaDig ::. Esteganografia em Mídias " +
                                                             "Digistais - UFF");
  }

  /************************************************************
   *               Fim do controle do layout
   *    Iníco dos métodos de operação da esteganografia
   ************************************************************/

  //  Arquivo de mídia selecionado para esteganografia
  //------------------------------------------------------
  private void abrirArquivoMidia_OK(String nomeArq)
  {
    jTxtAbrirMidia.setText(nomeArq);

    String extensao = TArquivo.extrairExtensao(nomeArq);
    if (extensao.compareTo("bmp")==0) arqMidia = new TBmp(nomeArq);
    else if  (extensao.compareTo("wav")==0) arqMidia = new TWav(nomeArq);
    else arqMidia = new TArquivo(nomeArq);     
    
    jlblInfoArqMidia.setText("Extensão: " + arqMidia.getExtensao() + 
                             " - Tamanho: " + 
                             TArquivo.formatTamanho(arqMidia.getTamanho()));
    
    Selecionar.setEnabled(true);
    jtxtaMsg.setEnabled(true);
    jtxtaMsg.setText("");
    jtxtArq.setText("");
    
    //Tipo de arquivo JPG, MP3 e AVI; Apenas esteganografia em final de arquivo.
    if ((arqMidia.getExtensao().compareTo("jpg")==0) ||
        (arqMidia.getExtensao().compareTo("jpeg")==0) ||
        (arqMidia.getExtensao().compareTo("mp3")==0) ||
        (arqMidia.getExtensao().compareTo("avi")==0))
    {
      jrdbFinalArq.setSelected(true);
      jrdbFinalArq.setEnabled(true);
      jrdbLsb.setEnabled(false);
      jrdbLsb2.setEnabled(false);
      jrdbLsb3.setEnabled(false);
      jrdbLsbn.setEnabled(false);
      jrdbLsbCiclico.setEnabled(false);
    }
    //Formato BMP e WAV; Todas as esteganografias liberadas
    else if ((arqMidia.getExtensao().compareTo("bmp")==0) ||
             (arqMidia.getExtensao().compareTo("wav")==0))
    {
      jrdbFinalArq.setEnabled(true);
      jrdbLsb.setEnabled(true);
      jrdbLsb2.setEnabled(true);
      jrdbLsb3.setEnabled(true);
      jrdbLsbn.setEnabled(true);
      jrdbLsbCiclico.setEnabled(true);
    }
    //Não deveria existir essa possibilidade
    else
    {
      jrdbFinalArq.setEnabled(false);
      jrdbLsb.setEnabled(false);
      jrdbLsb2.setEnabled(false);
      jrdbLsb3.setEnabled(false);
      jrdbLsbn.setEnabled(false);
      jrdbLsbCiclico.setEnabled(false);
      jbtnOcultar.setEnabled(false);
      Selecionar.setEnabled(false);
      jtxtaMsg.setEnabled(false);
    }
    
    verifiOpcEsteganografia();
    
  }

  //    Arquivo de midia selecionado para recuperação da esteganografia
  //------------------------------------------------------------------------
  private void abrirArquivoMidiaEsteg_OK(String nomeArq)
  {
    jtxtArqMidiaEsteg.setText(nomeArq);
    
    String extensao = TArquivo.extrairExtensao(nomeArq);
    if (extensao.compareTo("bmp")==0) arqMidia = new TBmp(nomeArq);
    else if  (extensao.compareTo("wav")==0) arqMidia = new TWav(nomeArq);
    else arqMidia = new TArquivo(nomeArq);     
    
    estegFinalArq = new TFinalArquivo(arqMidia);
    if (estegFinalArq.estaEsteganografado())
    {     
      jbtnRecuperar.setEnabled(true);
      String infoArqEsteg = "<html>Extensão: " + arqMidia.getExtensao() + 
                            " - Tamanho: " + 
                            TArquivo.formatTamanho(arqMidia.getTamanho()) +
                            "<br> Arquivo com dado Esteganografado<br>" + 
                            "Tipo de dado: ";
                            
      if (estegFinalArq.getTipoDado() == TFinalArquivo.TipoDadoOculto.ARQUIVO) 
         infoArqEsteg += "Arquivo";
      else 
         infoArqEsteg += "Mensagem de Texto";
      
      infoArqEsteg += "<br>Tamanho do dado oculto: " + 
                       TArquivo.formatTamanho(estegFinalArq.getTamanhoDado()) + 
                       "</html>";
                       
      jlblInfoArqMidiaEsteg.setText(infoArqEsteg );
    }   
    else
    {   
      estegFinalArq = null;
      byte [] dado = ((TArquivoMidia)arqMidia).getDadosMidia();
      if(TLSB.estaLsbEsteganografado(dado))
      {
        jbtnRecuperar.setEnabled(true);
        String infoArqEsteg = "<html>Extensão: " + arqMidia.getExtensao() + 
                            " - Tamanho: " + 
                            TArquivo.formatTamanho(arqMidia.getTamanho()) +
                            "<br> Arquivo com dado Esteganografado<br>" +
                            "Técnica utilizada: ";
        
        if(LSB.estaEsteganografado(dado))
        {
          infoArqEsteg += "LSB<br>";
          estegLsb = new LSB ((TArquivoMidia)arqMidia);
        }
        else if(LSBCiclico.estaEsteganografado(dado))
        {
          infoArqEsteg += "LSB Cíclico<br>";
          estegLsb = new LSBCiclico ((TArquivoMidia)arqMidia);
        }
        else if(LSB2.estaEsteganografado(dado))
        {
          infoArqEsteg += "LSB 2<br>";
          estegLsb = new LSB2 ((TArquivoMidia)arqMidia);
        }
        else if(LSB3.estaEsteganografado(dado))
        {
          infoArqEsteg += "LSB 3<br>";
          estegLsb = new LSB3 ((TArquivoMidia)arqMidia);
        }
        else if(LSBn.estaEsteganografado(dado))
        {
          infoArqEsteg += "LSB n; sendo n = " + LSBn.n(dado) + "<br>";
          estegLsb = new LSBn ((TArquivoMidia)arqMidia);
        }
        
        infoArqEsteg += "Tipo do conteúdo oculto: ";
        if(TLSB.tipoDadoEsteg(arqMidia.getConteudo())==TLSB.TipoDadoOculto.ARQUIVO)
          infoArqEsteg += "Arquivo<br>";
        else
          infoArqEsteg += "Mensagem de Texto<br>";
        
        jlblInfoArqMidiaEsteg.setText(infoArqEsteg );
      }
      else
      {        
        jbtnRecuperar.setEnabled(false);
        jlblInfoArqMidiaEsteg.setText("");
      }
    }
  }
  
  //  Arquivo a ser ocultado, selecionado
  //-------------------------------------------
  private void abrirArquivoOcultar_OK(String nomeArq)
  {
    jtxtaMsg.setText("");
    
    arqOcultar = new TArquivo(nomeArq);
    jlblTamArq.setText("Tamanho: " + 
                        TArquivo.formatTamanho(arqOcultar.getTamanho()));
    jtxtArq.setText(nomeArq);
  }
  
  private void jtxtaMsg_Alterou ()
  {
    jtxtArq.setText("");
    
    if (arqOcultar!=null) arqOcultar.limpar();
    jlblTamArq.setText("Tamanho: " + 
                       TArquivo.formatTamanho(jtxtaMsg.getText().length()));
  }
  
  private void verifiOpcEsteganografia ()
  {
    if (jrdbFinalArq.isSelected() && jrdbFinalArq.isEnabled())
    {
      estegFinalArq = new TFinalArquivo(arqMidia);
      jlblLimitArq.setText("Limite: " + 
                            TArquivo.formatTamanho(estegFinalArq.tamMaxDado()));
      jbtnOcultar.setEnabled(true);
    }
    else if (jrdbLsb.isSelected() && jrdbLsb.isEnabled())
    {
      if (jtxtArq.getText().trim().length()<=0) 
      {
        jlblLimitArq.setText("Limite: " + 
                              TArquivo.formatTamanho(
                              LSB.verificaMaxTamDadoArq(
                              arqMidia.getConteudo().length)));
                              
        jbtnOcultar.setEnabled(true);      
      }
      else
      {
        jlblLimitArq.setText("Limite: " +
                              TArquivo.formatTamanho(
                              LSB.verificaMaxTamDadoTxt(
                              arqMidia.getConteudo().length)));
                              
        jbtnOcultar.setEnabled(true);      
      }
    }
    else if (jrdbLsb2.isSelected() && jrdbLsb2.isEnabled())
    {
      if (jtxtArq.getText().trim().length()<=0) 
      {
        jlblLimitArq.setText("Limite: " + 
                              TArquivo.formatTamanho(
                              LSB2.verificaMaxTamDadoArq(
                              arqMidia.getConteudo().length)));
                              
        jbtnOcultar.setEnabled(true);      
      }
      else
      {
        jlblLimitArq.setText("Limite: " +
                              TArquivo.formatTamanho(
                              LSB2.verificaMaxTamDadoTxt(
                              arqMidia.getConteudo().length)));
                              
        jbtnOcultar.setEnabled(true);      
      }
    }    
    else if (jrdbLsb3.isSelected() && jrdbLsb3.isEnabled())
    {
      if (jtxtArq.getText().trim().length()<=0) 
      {
        jlblLimitArq.setText("Limite: " + 
                              TArquivo.formatTamanho(
                              LSB3.verificaMaxTamDadoArq(
                              arqMidia.getConteudo().length)));
                              
        jbtnOcultar.setEnabled(true);      
      }
      else
      {
        jlblLimitArq.setText("Limite: " +
                              TArquivo.formatTamanho(
                              LSB3.verificaMaxTamDadoTxt(
                              arqMidia.getConteudo().length)));
                              
        jbtnOcultar.setEnabled(true);      
      }
    }    
    else if (jrdbLsbn.isSelected() && jrdbLsbn.isEnabled())
    {
      if (jtxtArq.getText().trim().length()<=0) 
      {
        jlblLimitArq.setText("Limite: " +
                              TArquivo.formatTamanho(
                              LSBn.verificaMaxTamDadoArq(
                              arqMidia.getConteudo().length, 
                              (Integer)jSpn.getValue())));
                              
        jbtnOcultar.setEnabled(true);      
      }
      else
      {
        jlblLimitArq.setText("Limite: " +
                              TArquivo.formatTamanho(
                              LSBn.verificaMaxTamDadoTxt(
                              arqMidia.getConteudo().length, 
                              (Integer)jSpn.getValue())));
                              
        jbtnOcultar.setEnabled(true);      
      }
    }    
    else if (jrdbLsbCiclico.isSelected() && jrdbLsbCiclico.isEnabled())
    {
      if (jtxtArq.getText().trim().length()<=0) 
      {
        jlblLimitArq.setText("Limite: " + 
                              TArquivo.formatTamanho(
                              LSBCiclico.verificaMaxTamDadoArq(
                              arqMidia.getConteudo().length)));
        jbtnOcultar.setEnabled(true);      
      }
      else
      {
        jlblLimitArq.setText("Limite: " +
                              TArquivo.formatTamanho(
                              LSBCiclico.verificaMaxTamDadoTxt(
                              arqMidia.getConteudo().length)));
        jbtnOcultar.setEnabled(true);      
      }
    }
  }
  
  // Clique no botão recuperar dados esteganografado
  //-----------------------------------------------------
  private void jbtnRecuperar_OnClick (ActionEvent evt)
  {
    if (estegFinalArq!=null)
    {
      if (estegFinalArq.estaEsteganografado())
      {     
        if (estegFinalArq.getTipoDado() == TFinalArquivo.TipoDadoOculto.ARQUIVO)
        {
          if(salvaArq (estegFinalArq.recuperaDadoArquivo())==0)
          {
            JOptionPane.showMessageDialog(null, 
                              "Arquivo esteganografado recuperado com sucesso.", 
                              "Alerta", 
                              JOptionPane.INFORMATION_MESSAGE);
          }
          else
          {
            JOptionPane.showMessageDialog(null, 
                                      "Ocorreu um erro ao recuperar o arquivo.", 
                                      "Alerta", 
                                      JOptionPane.INFORMATION_MESSAGE);
          }
        }
        else 
        {
          jtxtaMsgRecuperada.setText(estegFinalArq.recuperaDadoTexto());
          JOptionPane.showMessageDialog(null, 
                                             "Mensagem recuperada com sucesso.", 
                                             "Alerta", 
                                             JOptionPane.INFORMATION_MESSAGE);         
        }
      }
      else
      {
        JOptionPane.showMessageDialog(null, 
                       "Houve problema na recuperação do dado esteganografado.", 
                       "Alerta", 
                       JOptionPane.INFORMATION_MESSAGE);
      }
    }
    else if(estegLsb!=null)
    {
      if(estegLsb.estaLsbEsteganografado())
      {
        if (estegLsb.getTipoDado() == TLSB.TipoDadoOculto.ARQUIVO)
        {     
          if(salvaArq (estegLsb.recuperaDadoArquivo())==0)
          {
            JOptionPane.showMessageDialog(null, 
                              "Arquivo esteganografado recuperado com sucesso.", 
                              "Alerta", 
                              JOptionPane.INFORMATION_MESSAGE);
          }
          else
          {
            JOptionPane.showMessageDialog(null, 
                                      "Ocorreu um erro ao recuperar o arquivo.", 
                                      "Alerta", 
                                      JOptionPane.INFORMATION_MESSAGE);
          }
        }
        else 
        {
          jtxtaMsgRecuperada.setText(estegLsb.recuperaDadoTexto());
          JOptionPane.showMessageDialog(null, 
                                             "Mensagem recuperada com sucesso.", 
                                             "Alerta", 
                                             JOptionPane.INFORMATION_MESSAGE);         
        }                                                    
      }
      else
      {
        JOptionPane.showMessageDialog(null, 
                       "Houve problema na recuperação do dado esteganografado.", 
                       "Alerta", 
                       JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }
  
  // Clique no botão ocultar
  //---------------------------
  private void jbtnOcultar_OnClick (ActionEvent evt)
  {
    //Esteganografia em final de arquivo
    if (jrdbFinalArq.isSelected() && jrdbFinalArq.isEnabled())
    {
      //Se tem mensagem digitada
      if(jtxtaMsg.getText().length()>0)
      {
        if(jtxtaMsg.getText().length()>estegFinalArq.tamMaxDado())
        {
          JOptionPane.showMessageDialog(null, 
                              "A mensagem digitada é maior do que o suportado.", 
                              "Alerta", 
                              JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
          if(salvaArq (estegFinalArq.ocultaDado(jtxtaMsg.getText()))==0)
          {
            JOptionPane.showMessageDialog(null, 
                                               "Arquivo gravadao com sucesso.", 
                                               "Alerta", 
                                               JOptionPane.INFORMATION_MESSAGE);
          }
          else
          {
            JOptionPane.showMessageDialog(null, 
                                         "Ocorreu um erro ao salvar o arquivo.", 
                                         "Alerta", 
                                         JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
      //Se foi selecionado um arquivo
      else if (arqOcultar!=null)
      {
        if(arqOcultar.getTamanho()>estegFinalArq.tamMaxDado())
        {
            JOptionPane.showMessageDialog(null, 
                            "O arquivo selecionado é maior do que o suportado.", 
                            "Alerta", 
                            JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
          if(salvaArq (estegFinalArq.ocultaDado(arqOcultar))==0)
          {
            JOptionPane.showMessageDialog(null, 
                                               "Arquivo gravadao com sucesso.", 
                                               "Alerta", 
                                               JOptionPane.INFORMATION_MESSAGE);
          }
          else
          {
            JOptionPane.showMessageDialog(null,
                                         "Ocorreu um erro ao salvar o arquivo.", 
                                         "Alerta", 
                                         JOptionPane.INFORMATION_MESSAGE);
          }
        }
        
      }
      //Esse de segurança, teóricamenete desnecessário
      else
      {
        JOptionPane.showMessageDialog(null, 
                          "Não foi encontrado nenhum dado para esteganografar.", 
                          "Alerta", 
                          JOptionPane.INFORMATION_MESSAGE);
      }
    }
    //Esteganografia LSB e seus derivados
    else
    {
      if (jrdbLsbCiclico.isSelected() && jrdbLsbCiclico.isEnabled())
      {
        estegLsb = new LSBCiclico((TArquivoMidia)arqMidia);
      }
      else if (jrdbLsbn.isSelected() && jrdbLsbn.isEnabled())
      {
        estegLsb = new LSBn((TArquivoMidia)arqMidia, (Integer)jSpn.getValue());
      }
      else if (jrdbLsb3.isSelected() && jrdbLsb3.isEnabled())
      {
        estegLsb = new LSB3((TArquivoMidia)arqMidia);
      }
      else if (jrdbLsb2.isSelected() && jrdbLsb2.isEnabled())
      {
        estegLsb = new LSB2((TArquivoMidia)arqMidia);
      }      
      else
      {
        estegLsb = new LSB((TArquivoMidia)arqMidia);
      }
      
      //Se tem mensagem digitada
      if(jtxtaMsg.getText().length()>0)
      {
        if(jtxtaMsg.getText().length()>estegLsb.tamMaxEsteg())
        {
          JOptionPane.showMessageDialog(null, 
                              "A mensagem digitada é maior do que o suportado.", 
                              "Alerta", 
                              JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
          if(salvaArq (estegLsb.ocultaDado(jtxtaMsg.getText()))==0)
          {
            JOptionPane.showMessageDialog(null, 
                                               "Arquivo gravadao com sucesso.", 
                                               "Alerta", 
                                               JOptionPane.INFORMATION_MESSAGE);
          }
          else
          {
            JOptionPane.showMessageDialog(null, 
                                         "Ocorreu um erro ao salvar o arquivo.", 
                                         "Alerta", 
                                         JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
      //Se foi selecionado um arquivo
      else if (arqOcultar!=null)
      {
        if(arqOcultar.getTamanho()>estegLsb.tamMaxEsteg())
        {
            JOptionPane.showMessageDialog(null, 
                            "O arquivo selecionado é maior do que o suportado.", 
                            "Alerta", 
                            JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
          if(salvaArq (estegLsb.ocultaDado(arqOcultar))==0)
          {
            JOptionPane.showMessageDialog(null, 
                                               "Arquivo gravadao com sucesso.", 
                                               "Alerta", 
                                               JOptionPane.INFORMATION_MESSAGE);
          }
          else
          {
            JOptionPane.showMessageDialog(null,
                                         "Ocorreu um erro ao salvar o arquivo.", 
                                         "Alerta", 
                                         JOptionPane.INFORMATION_MESSAGE);
          }
        }
      }
      //Esse de segurança, teóricamenete desnecessário
      else
      {
        JOptionPane.showMessageDialog(null, 
                          "Não foi encontrado nenhum dado para esteganografar.", 
                          "Alerta", 
                          JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }
  
  private int salvaArq (final TArquivo arq)
  {
    JFileChooser FcSave = new JFileChooser();
    FcSave.setAcceptAllFileFilterUsed(false);

    //Tipo predefinido
    FcSave.setFileFilter(new FileFilter()
    {
      @Override
      public String getDescription() { return "Formato obrigatório do arquivo" +
                                             " (*." + arq.getExtensao() + ")"; }
      
      @Override
      public boolean accept(File f)
      {
        if (f.isDirectory()) { return true; }
        else
        {
          String filename = f.getName().toLowerCase();
          return filename.endsWith("." + arq.getExtensao());
        }
      }
    });


    int returnVal = FcSave.showSaveDialog(painelPrincipal);
    if(returnVal == JFileChooser.APPROVE_OPTION)
    {
      //O método que fará o processamento do arquivo selecionado
      return arq.salvar(FcSave.getSelectedFile().getAbsolutePath());
    }

    return 1;

  }
  
  private void limpaTela ()
  {
    jSpn.setEnabled(jrdbLsbn.isSelected());   
    
  }
}