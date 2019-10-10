set title "ChangedFiles"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(339.6345633895722*x**0)+(-21340.58528356487*x**1)+(397986.4949580274*x**2)+(-3350842.6248663915*x**3)+(1.5390648535006145E7*x**4)+(-4.187652958397452E7*x**5)+(6.92646263196033E7*x**6)+(-6.827898899234763E7*x**7)+(3.68301321553027E7*x**8)+(-8356031.179884534*x**9)
set xrange [0:1]
set yrange [0:1]
plot f(x)