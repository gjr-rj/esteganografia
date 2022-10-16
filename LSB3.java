class LSB3 extends LSBn
{  
  //A técnica LSB é representada por 0b00000110
  static byte tipoEsteganografia_LSB = (byte)0b00000110;
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
    return ((tamConteudo - (13*8))*3)/8;
  }
  
  static long verificaMaxTamDadoTxt(long tamConteudo)
  {
    return ((tamConteudo - (9*8))*3)/8;
  }
  
  public LSB3 (TArquivoMidia arquivo)
  {
    super(arquivo, 3);
  }
       
  public LSB3 (TArquivoMidia arquivo, int n)
  {    
    super(arquivo, 3);    
  }
}