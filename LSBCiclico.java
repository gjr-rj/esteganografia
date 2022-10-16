class LSBCiclico extends TLSB
{
  //A técnica LSB Ciclico é representada por 0b00000010
  static byte tipoEsteganografia_LSB = (byte)0b00010000;
  static boolean estaEsteganografado (byte[] conteudoMidia)
  {
    byte b = 0;
    for(int i=conteudoMidia.length-40; i<conteudoMidia.length-32; i++)
    {
      b = (byte)(b<<1);
      b = (byte)(b | (conteudoMidia[i] & 0b00000001));      
    }
        
    return ((b&0b11111110)==tipoEsteganografia_LSB) && 
             TLSB.estaLsbEsteganografado(conteudoMidia);
  }
  
  static long verificaMaxTamDadoArq(long tamConteudo)
  {
    return ((tamConteudo - (13*8))*7)/8;
  }
  
  static long verificaMaxTamDadoTxt(long tamConteudo)
  {
    return ((tamConteudo - (9*8))*7)/8;
  }
  
  public LSBCiclico (TArquivoMidia arquivo)
  {
    super(arquivo);
    recalcQtdeByteEsteg();
  }
  
  //Métodos Púlbicos
  //--------------------
  public long tamMaxConteudo()
  {
    return ((tamMaxEsteg() - tamRodape())*7)/8;
  }
  
  public long qtdeByteEsteg(long tamDado)
  {
    return tamMaxEsteg() - tamRodape();
  }
  
  public TArquivoMidia ocultaDado (String msg)
  {
    if (msg.length()> tamMaxEsteg()) return null;
    
    rodapeTxt(msg.length(), tipoEsteganografia_LSB);
    byte[] msgByte = msg.getBytes();
    
    //Adicionando o rodapé
    zeraPosByte();
    setPosBit(1);
    
    for (int i=0; i<msgByte.length; i++)
    {
      for (int j=0; j<8; j++)
      {
        //0b10000000 para pegar o bit mais significativo
        int val = ((byte)(msgByte[i]<<j)) & ((byte)0b10000000);
        alteraBit(val!=0);
        long proxByte = incPosByte();
        if(proxByte==0) incPosBit();
      }
    }
    
    return altDadoMidia();
  }
  
  public TArquivoMidia ocultaDado (TArquivo arquivo)
  {
    if (arquivo.getTamanho()> tamMaxEsteg()) return null;
    
    rodapeArq (arquivo.getTamanho(), arquivo.getExtensao(), tipoEsteganografia_LSB);    
    
    
    byte[] arqByte = arquivo.getConteudo();
    
    //Adicionando o rodapé
    zeraPosByte();
    setPosBit(1);
    
    for (int i=0; i<arqByte.length; i++)
    {
      for (int j=0; j<8; j++)
      {
        //0b10000000 para pegar o bit mais significativo
        int val = ((byte)(arqByte[i]<<j)) & ((byte)0b10000000);
        alteraBit(val!=0);
        long proxByte = incPosByte();
        if(proxByte==0) incPosBit();
      }
    }
    
    return altDadoMidia();
  }
  
  public String recuperaDadoTexto () //Recupera mensagem Txt do arquivo
  {
    long tamMsg = tamDadoOculto();
    byte[] b = new byte[(int)tamMsg];
    
    zeraPosByte();
    setPosBit(1);
    
    for(int i=0; i<tamMsg; i++)
    {
      b[i] = 0;
      for(int j=0; j<8; j++)
      {
        boolean bit = getBit();
        b[i] = (byte)(b[i]<<1);
        if(bit) b[i] = (byte)(b[i] | 1);
        else    b[i] = (byte)(b[i] | 0);
        
        long proxByte = incPosByte();
        if(proxByte==0) incPosBit();
      }
    }
    
    return new String(b);
  }
  
  public TArquivo recuperaDadoArquivo () //Recupera arquivo do arquivo
  {    
    long tamMsg = tamDadoOculto();
    byte[] b = new byte[(int)tamMsg];
    
    String extensao = extensaoDadoOculo();
    
    zeraPosByte();
    setPosBit(1);
    
    for(int i=0; i<tamMsg; i++)
    {
      b[i] = 0;
      for(int j=0; j<8; j++)
      {
        boolean bit = getBit();

        b[i] = (byte)(b[i]<<1);
        if(bit) b[i] = (byte)(b[i] | 1);
        else    b[i] = (byte)(b[i] | 0);
        
        long proxByte = incPosByte();
        if(proxByte==0) incPosBit();
      }
    }
    
    TArquivo arOculto = new TArquivo();
    arOculto.setConteudo(b);
    arOculto.setExtensao (extensao);
    return arOculto;
  }
}