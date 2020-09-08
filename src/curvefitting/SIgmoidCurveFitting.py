import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

def sigmoid(x,a,b,c):
    y=c/(1+np.exp(a*x+b))
    return y

xdata=np.array([1,2,3,4,5, 6, 7, 8, 9, 10])
ydata=np.array([2,3,5,4,10,16,23,31,36,34])

popt, pcov=curve_fit(sigmoid, xdata, ydata,bounds=([-1.,-1.,-np.inf],[10.,10.,np.inf]))

#获取popt中的拟合系数
a=popt[0]
b=popt[1]
c=popt[2]

#拟合y值
x=np.array([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20])
yvals=sigmoid(x,*popt)
print('函数y=',c,'/(1+e^(',a,'*(x+',b,')))')
print('yvals:',yvals)

#绘图
plot1=plt.plot(xdata,ydata,'s',label='origin values')
plot2=plt.plot(x,yvals,'r',label='polyfit values')
plt.xlabel('x')
plt.ylabel('y')
plt.legend(loc=4)
plt.title('curve_fit')
plt.show()





