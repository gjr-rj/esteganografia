class TFinalArquivo
{
  static enum TipoDadoOculto { ARQUIVO, MSG_TXT; }
  
  //    Variávei privadas
  //------------------------
  private TArquivo VPContainer;
  private boolean VPEsteganografado;
  private TipoDadoOculto VPTipoEsteganografia;
  private long VPTamEsteg;
  private String VPExtensaoEsteg;

  //  Métodos privados
  //------------------------------------
  private byte[] rodapeArq(long tamDado, String extensao)
  {
   byte[] t = TArquivo.longToBytes(tamDado);
   //Trabalharemos apenas com 4bytes
   byte[] tb = {t[4], t[5], t[6], t[7] };
   
   //Assinatura mais tipo Arquivo
   // O mesmo que ("ATCUF").getBytes(), porém, com menos processamento
   byte[] a = {65, 84, 67, 85, 70};
   
   //Limitando a extensão a 4 caracters.
   if (extensao.length()>4) extensao = extensao.substring(4);
   
   //Ajustando a extensão para conter 4 caracteres
   while (extensao.length()<4) { extensao += " "; }
   
   return TArquivo.byteArrayConcat( extensao.getBytes(), TArquivo.byteArrayConcat(tb, a));
  }
  
  private byte[] rodapeTxt(long tamDado)
  {
   byte[] t = TArquivo.longToBytes(tamDado);
   //Trabalharemos apenas com 4bytes
   byte[] tb = {t[4], t[5], t[6], t[7] };
   
   //Assinatura mais tipo Mensagem
   // O mesmo que ("MTCUF").getBytes(), porém, com menos processamento
   byte[] a = {77, 84, 67, 85, 70};
   
   return TArquivo.byteArrayConcat(tb, a);
  }
  
  private void verificaEsteganografia ()
  {
    byte[] b = VPContainer.getConteudo();
    VPEsteganografado = (b[b.length-1]==70) && (b[b.length-2]==85) && (b[b.length-3]==67) && (b[b.length-4]==84);
    if(VPEsteganografado)
    {
      if (b[b.length-5]==77)
        VPTipoEsteganografia = TipoDadoOculto.MSG_TXT;
      else
      {
        VPTipoEsteganografia = TipoDadoOculto.ARQUIVO;
        
        byte[] e = {b[b.length-13], b[b.length-12], b[b.length-11], b[b.length-10]};
        VPExtensaoEsteg = new String(e);
      }
      
      //Tamanho da dado esteganografado
      byte[] t = {0, 0, 0, 0, b[b.length-9], b[b.length-8], b[b.length-7], b[b.length-6]};
      VPTamEsteg = TArquivo.bytesToLong(t);
    }      
  }
  
  // Construtor
  //------------------------------------
  public TFinalArquivo (TArquivo Container)
  {
    VPContainer = Container;
    verificaEsteganografia();
  }

  //  Propriedades
  //----------------------------------
  public long getTamanhoDado()
  {
    return VPTamEsteg;
  }

  public long tamMaxDado ()
  {
    //2^32, pois foram definidos 4 bytes para armazenar o tamanho do dado oculto
    return (new Double(Math.pow(2,32))).longValue();
  }

  public boolean estaEsteganografado ()
  {
    return VPEsteganografado;
  }

  public TipoDadoOculto getTipoDado ()
  {
    return VPTipoEsteganografia;
  }

  //  Métodos Públicos
  //----------------------------------
  public TArquivo ocultaDado (String msg) //Oculta mensagem Txt no arquivo
  {
    byte[] msgByte = msg.getBytes();
    byte[] novoConteudo = TArquivo.byteArrayConcat(VPContainer.getConteudo(), msgByte);
    novoConteudo = TArquivo.byteArrayConcat(novoConteudo, rodapeTxt(msgByte.length));
    
    VPContainer.setConteudo(novoConteudo);
    return VPContainer;
  }

  public TArquivo ocultaDado (TArquivo arquivo)  //Oculta arquivo no arquivo
  {
    byte[] novoConteudo = TArquivo.byteArrayConcat(VPContainer.getConteudo(), arquivo.getConteudo());
    novoConteudo = TArquivo.byteArrayConcat(novoConteudo, rodapeArq(arquivo.getTamanho(), arquivo.getExtensao()));
      
    VPContainer.setConteudo(novoConteudo);
    return VPContainer;
  }

  public String recuperaDadoTexto () //Recupera mensagem Txt do arquivo
  {
    byte[] msgByte = new byte[(int)VPTamEsteg];
    byte[] b = VPContainer.getConteudo();
    System.arraycopy(b, (int)(b.length - VPTamEsteg - 9), msgByte, 0, (int)VPTamEsteg);
   
    return new String(msgByte);
  }

  public TArquivo recuperaDadoArquivo () //Recupera arquivo do arquivo
  {
    byte[] arqByte = new byte[(int)VPTamEsteg]; //Tamanho
    byte[] b = VPContainer.getConteudo();       //Conteúdo oculto
    
    System.arraycopy(b, (int)(b.length - VPTamEsteg - 13), arqByte, 0, (int)VPTamEsteg);
     
    TArquivo arOculto = new TArquivo();
    arOculto.setConteudo(arqByte);
    arOculto.setExtensao (VPExtensaoEsteg);
    return arOculto;
  }
}