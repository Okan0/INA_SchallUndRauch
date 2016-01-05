package tim.FFT;

import tim.FFT.Complex;

// ***
// f[]: Eingabe: zu transformierende Daten
//      Ausgabe: Ergebnis der Transformation
// sign=-1: Hintransformation; sign=1: Ruecktransformation

public class FFT {
public static void FFT(Complex[] f, int sign) {
  int N=f.length;       // Java-Arrays kennen ihre Laenge
  int mask;
  // *** Teste, ob N 2er-Potenz ist
  for(mask=1; mask<N; mask <<= 1)       ;
  if(mask != N)
    throw new RuntimeException("N = " + " ist keine 2er-Potenz !");
  // *** Teile Daten durch sqrt(N)
  double isqrtN = 1/Math.sqrt(N);
  for(int r=0; r<N; r++)
    f[r] = f[r].times(isqrtN);
  // *** Bit-Umkehr
  for(int t=0, r=0; r<N; r++) {
     if(t > r) {        // Vertausche f[r] und f[t]
        Complex temp = f[r];
        f[r] = f[t];
        f[t] = temp;
      }
     mask = N;          // Bit-umgekehrtes Inkrement von t
     do {
        mask >>= 1;
        t ^= mask;
      } while(((t & mask) == 0) && (mask != 0));
   }
  // *** Danielson-Lanczos Teil
  int n, no2 = 1;
  for(; (n=(no2 << 1)) <= N;) {
     Complex W = new Complex(Math.cos(2*Math.PI/n),
                 sign*Math.sin(2*Math.PI/n));           // W_n
     Complex Wk = new Complex(1, 0);
     for(int k=0; k<no2; k++) {
        for(int l=k; l<N; l+=n) {
           Complex temp = Wk.times(f[l+no2]);
           f[l+no2] = f[l].minus(temp);
           f[l]     = f[l].plus(temp);
         }
        Wk = Wk.times(W);       // Wk = W^k
      }
     no2 = n;
   }
  System.out.println("Analyse:\n" + f.toString());
 }

////////////////////////////// Test-Teil

// Hauptprogramm

//public static void main(String[] args) {
//  double[] Daten = {1, 2, 3, 4, 3, 2, 1, Math.PI};
//  int N=Daten.length;           // Java-Arrays kennen ihre Laenge
//  Complex[] f = new Complex[N];
//
//  for(int r=0; r<N; r++)
//    f[r] = new Complex(Daten[r], 0);
//  FFT(f, -1);                   // Hintransformation
//  for(int r=0; r<N; r++)
//    System.out.println("^f[" + r + "] = " + f[r]);
//  FFT(f, 1);                    // Rueckransformation
//  for(int r=0; r<N; r++)
//    System.out.println("f[" + r + "] = " + f[r]
//         + "\tOriginaldaten: " + Daten[r]);
// }
}