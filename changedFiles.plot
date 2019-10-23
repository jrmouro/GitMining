set title "ChangedFiles"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(17870.579430012538*x**0)+(-289629.4589574027*x**1)+(2044237.0363362967*x**2)+(-8243860.3251638375*x**3)+(2.0929170604662497E7*x**4)+(-3.469010933599181E7*x**5)+(3.7550811303812206E7*x**6)+(-2.5611182108490888E7*x**7)+(9994300.469194036*x**8)+(-1701608.6242061192*x**9)
set xrange [0:1]
set yrange [0:1]
plot f(x)
