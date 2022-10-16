class LSBn extends TLSB
{
  private int VPN;
  private byte tipoEsteganografia_LSB_n;

  private final int MIN_N = 2;
  private final int MAX_N = 7;
  
  //A técnica LSB é representada por 0b00001000
  static byte tipoEsteganografia_LSB = (byte)0b00001000;
  static boolean estaEsteganografado (byte[] conteudoMidia)
  {
    byte b = 0;
    for(int i=conteudoMidia.length-40; i<conteudoMidia.length-32; i++)
    {
      b = (byte)(b<<1);
      b = (byte)(b | (conteudoMidia[i] & 0b00000001));      
    }
        
    return ((b&0b11111000)==tipoEsteganografia_LSB) && 
             TLSB.estaLsbEsteganografado(conteudoMidia);
  }
  
  static int n (byte[] conteudoMidia)
  {
    int b = 0;    
    for(int i=conteudoMidia.length-40; i<conteudoMidia.length-33; i++)
    {
      b = b<<1;
      b = b | (conteudoMidia[i] & 0b00000001);      
    }
        
    return b;
  }
  
  static long verificaMaxTamDadoArq(long tamConteudo, int n)
  {
    return ((tamConteudo - (13*8))*n)/8;
  }
  
  static long verificaMaxTamDadoTxt(long tamConteudo, int n)
  {
    return ((tamConteudo - (9*8))*n)/8;
  }
  
  public LSBn (TArquivoMidia arquivo, int n)
  {
    super(arquivo);    
    VPN = n;
    if(VPN<MIN_N) VPN = MIN_N;
    else if (VPN>MAX_N) VPN = MAX_N;
    tipoEsteganografia_LSB_n = (byte)(VPN<<1);
    recalcQtdeByteEsteg();
  }
       
  public LSBn (TArquivoMidia arquivo)
  {    
    super(arquivo);    
    VPN = n(arquivo.getDadosMidia());
    recalcQtdeByteEsteg();
  }
  
  //Métodos Púlbicos
  //--------------------
    
  public long tamMaxConteudo()
  {
    return ((tamMaxEsteg() - tamRodape())*VPN)/8;
  }
  
  public long qtdeByteEsteg(long tamDado)
  {
    if (VPN == 0) VPN =1;    
    float qtde = (((long)(((tamMaxEsteg() - tamRodape())*VPN)/8))*8)/VPN;    
    if(qtde==(long)qtde) return (long)qtde;
    else return (long)(qtde+1);
  }
  
  public TArquivoMidia ocultaDado (String msg)
  {
    if (msg.length()> tamMaxEsteg()) return null;
    
    rodapeTxt(msg.length(), tipoEsteganografia_LSB_n);
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
                
        if (getPosBit()>=VPN)
        {
          setPosBit(1);
          incPosByte();
        }
        else
        {          
          incPosBit();
        }
      }
    }
    
    return altDadoMidia();
  }
  
  public TArquivoMidia ocultaDado (TArquivo arquivo)
  {
    if (arquivo.getTamanho()> tamMaxEsteg()) return null;
    
    rodapeArq (arquivo.getTamanho(), arquivo.getExtensao(), tipoEsteganografia_LSB_n);
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
                
        if (getPosBit()>=VPN)
        {
          setPosBit(1);
          incPosByte();
        }
        else
        {          
          incPosBit();
        }
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
        
        if (getPosBit()>=VPN)
        {
          setPosBit(1);
          incPosByte();
        }
        else
        {          
          incPosBit();
        }
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
        
        if (getPosBit()>=VPN)
        {
          setPosBit(1);
          incPosByte();
        }
        else
        {          
          incPosBit();
        }
      }
    }
    
    TArquivo arOculto = new TArquivo();
    arOculto.setConteudo(b);
    arOculto.setExtensao (extensao);
    return arOculto;
  } 
}