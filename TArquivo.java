import java.nio.file.Files;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

public class TArquivo
{
  //    Métodos estáticos
  //------------------------
  public static byte[] byteArrayConcat (byte[] array1, byte[] array2)
  {
    //Novo array com tamanho total dos 2 array passados como parametro
    byte[] arrayResult = new byte[array1.length + array2.length];
    
    //Copiando o primeiro array para o novo a partir da posição 0
    System.arraycopy(array1, 0, arrayResult, 0, array1.length);
    //Copiando o segundo array para o novo a partir da posição array1.length
    System.arraycopy(array2, 0, arrayResult, array1.length, array2.length);
      
    return arrayResult;
  }

  public static byte[] longToBytes(long val) 
  {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.putLong(val);
    return buffer.array();
  }

  public static long bytesToLong(byte[] bytes) 
  {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.put(bytes);
    buffer.flip();//need flip
    return buffer.getLong();
  }
  
  static String formatTamanho (long tamanho)
  {
    if (tamanho<=1024) { return Long.toString(tamanho) + " b"; }

    double tamTemp =  tamanho/1024;
    if (tamTemp<=1024) { return String.format("%.2f", tamTemp ) + " Kb"; }

    tamTemp =  tamTemp/1024;
    if (tamTemp<=1024) { return String.format("%.2f", tamTemp ) + " Mb"; }

    tamTemp =  tamTemp/1024;
    if (tamTemp<=1024) { return String.format("%.2f", tamTemp ) + " Gb"; }

    tamTemp =  tamTemp/1024;
    return String.format("%.2f", tamTemp ) + " Tb";
  }

  static String extrairExtensao (String nomeArq)
  {
    return nomeArq.substring(nomeArq.lastIndexOf(".")+1, nomeArq.length()).toLowerCase();
  }

  static String extrairPath (String nomeArq)
  {
    return nomeArq.substring(0, nomeArq.lastIndexOf("/") + nomeArq.lastIndexOf("\\")+1);
  }

  //    Variávei privadas
  //------------------------
  private long VPTamanho;
  private byte[] VPConteudo;
  private String VPExtensao;
  private String VPPath;

  // Construtores
  //------------------------------------
  public TArquivo ()
  {
    limpar();
  }

  public TArquivo (String nomeArquivo)
  {
    VPExtensao = this.extrairExtensao(nomeArquivo);
    VPPath = this.extrairPath(nomeArquivo);
    abrir (nomeArquivo);
  }

  //  Propriedades
  //----------------------------------
  public void setExtensao (String extensao)
  {
    VPExtensao = extensao.trim();
  }
  
  public String getExtensao ()
  {
    return VPExtensao;
  }

  public long getTamanho ()
  {
    return VPTamanho;
  }

  public byte[] getConteudo ()
  {
    return VPConteudo;
  }

  public void setConteudo (byte[] conteudo)
  {
    VPConteudo = conteudo;
    VPTamanho = conteudo.length;
  }

  //  Métodos Públicos
  //---------------------------------- 
  public void limpar ()
  {
    VPTamanho = 0;
    VPConteudo = null;
    VPExtensao = "";
    VPPath = "";
  }

  public int abrir(String nomeArquivo)
  {
    try
    {
      Path path = Paths.get(nomeArquivo);
      VPConteudo = Files.readAllBytes(path);
      VPTamanho = VPConteudo.length;

      return 0;
    }
    catch (IOException e)
    {
      return 1;
    }
  }

  public int salvar (String nomeArq)
  {
  //Incluindo a extensão original
  if (!nomeArq.endsWith("." + this.getExtensao())) nomeArq += "." + this.getExtensao(); 
    try
    {
      Path path = Paths.get(nomeArq);
      Files.write(path, VPConteudo);
      return 0;
    }
    catch (IOException e)
    {
      return 1;
    }
  }
}
