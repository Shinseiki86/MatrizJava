package matriz;
import java.math.BigDecimal;

/**
 * A class that convert a given BigDecimal number into fraction, which
 * has numerador and denominador. The algorithm uses to convert decimal to
 * fraction is from the following website:
 * http://www.docstoc.com/docs/88518669/Algorithm-To-Convert-A-Decimal-To-A-Fraccion
 * 
 * @author Su Khai Koh
 */
public class Fraccion {

    private long numerador;
    private long denominador;
    private static final BigDecimal EPSILON = new BigDecimal(0.000000001);
    
    public Fraccion(String frac){
        if(frac.contains("/")){
            String number[] = frac.split("/");
            long n=Math.round(Double.parseDouble(number[0])),
                 d=Math.round(Double.parseDouble(number[1]));
            
            if (d < 0){
                n*=-1;
                d*=-1;
            }
            
            numerador=n;
            denominador=d;
        }
        simplificar();
    }
    
    /**
     * Convert the given decimal number to fraction.
     * @param number decimal number to be converted to fraction
     */
    public Fraccion(BigDecimal number) {
         
        BigDecimal z = number,
                   d0 = BigDecimal.ZERO,
                   d1 = BigDecimal.ONE,
                   d2 = BigDecimal.ONE,
                   n, result;
        if (!number.equals(BigDecimal.ZERO)){
            do {
                // z = 1 / [z - int(z)]
                BigDecimal den = z.subtract(new BigDecimal(z.toBigInteger()));
                try{
                z = BigDecimal.ONE.divide(den, 10, BigDecimal.ROUND_HALF_UP);
                } catch(ArithmeticException ex){
                    ex.getMessage();
                    System.err.println("number="+number+" : "+ den);
                }
                // d2 = d1 * int(z) + d0
                d2 = d1.multiply(new BigDecimal(z.toBigInteger())).add(d0);

                // n = round(int(number * d2))      round to nearest integer
                n = new BigDecimal(number.multiply(d2).
                        add(new BigDecimal(0.5)).toBigInteger());
                result = n.divide(d2, 10, BigDecimal.ROUND_HALF_UP);

                d0 = d1;
                d1 = d2;
            } while (number.subtract(result).abs().compareTo(EPSILON) > 0);

            numerador = (number.signum() == -1 ? n.toBigInteger().negate() : n.toBigInteger()).longValue();
            denominador = (d2.toBigIntegerExact().abs()).longValue();
        } else {
            numerador = 0;
            denominador = 1;
        }
        simplificar();
    }
    /**
     * Return the numerador of the fraction.
     * @return the numerador of the fraction
     */
    public long getNumerator() {
        return numerador;
    }
    
    /**
     * Return the denominador of the fraction.
     * @return the denominador of the fraction
     */
    public long getDenominator() {
        return denominador;
    }
    
    /**
     * Override the method to print the string in the format like
     * numerador/denominador
     * @return 
     */
    @Override
    public String toString() {
        if(numerador == 0)
            return "\r\n0";
        if(denominador == 1)
            return "\r\n"+numerador;
        if(Math.abs(numerador) == denominador){
            String signo = "";
            if (numerador<0)
                signo = "\r\n-";
            return signo + "1";
        } else
            return numerador +"\r\n/\r\n"+ denominador;
    }

    public final void simplificar(){
        long dividir=mcd();
        if (dividir != 0){
           numerador /= dividir;
           denominador /= dividir;
        }
    }
  
    private long mcd(){
        long u=Math.abs(numerador);
        long v=Math.abs(denominador);
        if(v==0){
            return u;
        }
        long r;
        while(v!=0){
            r=u%v;
            u=v;
            v=r;
        }
        return u;
    }

}