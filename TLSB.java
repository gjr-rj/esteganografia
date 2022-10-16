abstract class TLSB
{
  static enum TipoDadoOculto { ARQUIVO, MSG_TXT; }
  
  //    Variávei privadas
  //------------------------
  private TArquivoMidia VPContainer;
  private byte[] VPConteudo;
  private boolean VPEsteganografado;
  private long VPQtdeByteEsteg;
  private String VPExtensaoEsteg;
  private TipoDadoOculto VPTipoEsteganografia;
    
  private long VPPosByte;
  private int VPPosBit;
  
  //Métodos Estáticos
  //------------------    
  static TipoDadoOculto tipoDadoEsteg (byte[] conteudoMidia)
  {
    int tipo = conteudoMidia[conteudoMidia.length-33] & 0b00000001;
    if(tipo==0) return TipoDadoOculto.MSG_TXT;
    else return  TipoDadoOculto.ARQUIVO;
  }
    
  static boolean estaLsbEsteganografado(byte[] conteudoMidia)
  {
    byte[] b = {0, 0, 0, 0};
    for(int i=0, pos=conteudoMidia.length-32; i<4; i++)
    {
      for(int j=0; j<8; j++, pos++)
      {
        b[i] = (byte)(b[i]<<1);
        b[i] = (byte)(b[i] | (conteudoMidia[pos] & 0b00000001));
      }
    }
    
    return ((b[0]==84)&&(b[1]==67)&&(b[2]==85)&&(b[3]==70));
  }
  
  static int byteRotateLeft (int value, int distance)
  {
    if ((distance>7)||(distance<1)) return value;
    return (value << distance) | (value >> (8-distance));
  }
   
  static int byteRotateRigt (int value, int distance)
  {
    if ((distance>7)||(distance<1)) return value;
    return (value >> distance) | (value << (8 - distance));
  }
  
  // Construtor
  //------------------------------------
  public TLSB (TArquivoMidia arquivo)
  {
    VPContainer = arquivo;
    VPConteudo = arquivo.getDadosMidia();
    VPEsteganografado = estaLsbEsteganografado(VPConteudo);
    VPTipoEsteganografia = tipoDadoEsteg (VPConteudo);
    VPQtdeByteEsteg = qtdeByteEsteg(VPConteudo.length);
  }
  
  //Métodos Abstratos
  //-------------------------------------
  abstract TArquivoMidia ocultaDado (String msg);
  abstract TArquivoMidia ocultaDado (TArquivo arquivo);
  abstract String recuperaDadoTexto ();
  abstract TArquivo recuperaDadoArquivo ();
  abstract long tamMaxConteudo();
  abstract long qtdeByteEsteg(long tamDado);
  
  //Métodos Protegidos
  //-------------------------------------
  protected void recalcQtdeByteEsteg()
  {    
    VPQtdeByteEsteg = qtdeByteEsteg(VPConteudo.length);
  }
  protected long tamMaxEsteg()
  {
    return VPConteudo.length;
  }
  protected int tamRodape()
  {         
    if(VPTipoEsteganografia==TipoDadoOculto.ARQUIVO) return 13*8;
    else return 9*8;         
  }
    
  protected void alteraBit(boolean val)
  {
    int valByte;
    if (val) valByte = 0b10000000;
    else valByte = 0b01111111;
           
    valByte = byteRotateLeft(valByte, VPPosBit);
    
    if (val) VPConteudo[(int)VPPosByte] = (byte)(VPConteudo[(int)VPPosByte] | valByte);
    else VPConteudo[(int)VPPosByte] = (byte)(VPConteudo[(int)VPPosByte] & valByte);    
  }    
  
  protected boolean getBit()
  {
    int valByte = 0b10000000;          
    valByte = byteRotateLeft(valByte, VPPosBit);
    int val = 0xff & VPConteudo[(int)VPPosByte];
    if ((val & valByte)==0) return false; 
    else return true; 
  }
  
  protected long tamDadoOculto()
  {
    long b=0;
    for(int i=VPConteudo.length-72; i<VPConteudo.length-40; i++)
    {
        b = b<<1;
        b = b | (VPConteudo[i] & 0b00000001);
    }
    
    return b;
  }
  
  protected String extensaoDadoOculo()
  {
    byte[] b = {0, 0, 0, 0};
    for(int i=0, pos=VPConteudo.length-104; i<4; i++)
    {
      for(int j=0; j<8; j++, pos++)
      {
        b[i] = (byte)(b[i]<<1);
        b[i] = (byte)(b[i] | (VPConteudo[pos] & 0b00000001));
      }
    }
    
    return new String(b);
  }
    
  protected void rodapeTxt (long tamDado, byte tecnica)
  {     
    VPTipoEsteganografia = TipoDadoOculto.MSG_TXT;
    VPQtdeByteEsteg = qtdeByteEsteg(VPConteudo.length);
    
    //O Zero no final indica Mensagem TXT
    tecnica = (byte)(tecnica & (byte)0b11111110);
    
    //incluindo no rodapé o tamanho da mensagem esteganografada
    byte[] t = TArquivo.longToBytes(tamDado);
    //Trabalharemos apenas com 4bytes
    byte[] tb = {t[4], t[5], t[6], t[7] };
    
    //Assinatura mais tipo Mensagem
    // O mesmo que ("?TCUF").getBytes(), porém, com menos processamento
    byte[] a = {tecnica, 84, 67, 85, 70};
    
    //Array com os bytes referentes ao rodapé
    t = TArquivo.byteArrayConcat(tb, a);
    
    //Adicionando o rodapé
    VPPosBit = 1;
    VPPosByte = VPConteudo.length - (9*8 /*tamanho rodapé texto*/);
    
    for (int i=0; i<t.length; i++)
    {     
      for (int j=0; j<8; j++)
      {       
        int val = ((byte)(t[i]<<j)) & ((byte)0b10000000);
            
        alteraBit(val!=0);  
        VPPosByte++;  
      }    
    }
  }
  
  protected void rodapeArq (long tamDado, String extensao, byte tecnica)
  {         
    VPTipoEsteganografia = TipoDadoOculto.ARQUIVO;
    VPQtdeByteEsteg = qtdeByteEsteg(VPConteudo.length);

    //O 1 no final indica arquvo oculro
    tecnica = (byte)(tecnica | (byte)0b00000001);
    
    //incluindo no rodapé o tamanho da mensagem esteganografada
    byte[] t = TArquivo.longToBytes(tamDado);
    //Trabalharemos apenas com 4bytes
    byte[] tb = {t[4], t[5], t[6], t[7] };
    
    //Assinatura mais tipo Mensagem
    // O mesmo que ("?TCUF").getBytes(), porém, com menos processamento
    byte[] a = {tecnica, 84, 67, 85, 70};
    
    //Array com os bytes referentes ao rodapé
    t = TArquivo.byteArrayConcat(tb, a);
    
    //Limitando a extensão a 4 caracters.
    if (extensao.length()>4) extensao = extensao.substring(4);
    //Ajustando a extensão para conter 4 caracteres
    while (extensao.length()<4) { extensao += " "; }
    
    t = TArquivo.byteArrayConcat(extensao.getBytes(), t);
    
    //Adicionando o rodapé
    VPPosBit = 1;
    VPPosByte = VPConteudo.length - (13 * 8 /*Tamanho rodapé arq*/);

    for (int i=0; i<t.length; i++)
    {     
      for (int j=0; j<8; j++)
      {       
        int val = ((byte)(t[i]<<j)) & ((byte)0b10000000);
        alteraBit(val!=0);  
        VPPosByte++;  
      }    
    }
  }
  
  public boolean estaLsbEsteganografado()
  {
    byte[] b = {0, 0, 0, 0};
    for(int i=0, pos=VPConteudo.length-32; i<4; i++)
    {
      for(int j=0; j<8; j++, pos++)
      {
        b[i] = (byte)(b[i]<<1);
        b[i] = (byte)(b[i] | (VPConteudo[pos] & 0b00000001));
      }
    }
    
    return ((b[0]==84)&&(b[1]==67)&&(b[2]==85)&&(b[3]==70));
  }
  
  protected TArquivoMidia altDadoMidia()
  {
    VPContainer.setDadoMidia(VPConteudo);
    VPContainer.altDadoMidiaArq();
    return VPContainer;
  }
  
  public void zeraPosByte()
  {
    VPPosByte = 0;
  }
  
  public long incPosByte()
  {
    VPPosByte ++;
    if (VPPosByte>=VPQtdeByteEsteg) VPPosByte = 0;
    return VPPosByte;
  }
  
  public void setPosBit(int val)
  {
    if(val<1) VPPosBit = 1;
    //Não faz sentido utilizar esteganografia no bit mais significativo.
    else if (val>7) VPPosBit = 7;
    else VPPosBit = val;
  }
  
  public int getPosBit() { return VPPosBit; }
  
  public void incPosBit () { setPosBit(VPPosBit+1); }
  
  public TipoDadoOculto getTipoDado()
  {
    return VPTipoEsteganografia;
  }
}