import numpy as np
import sys
from scipy.optimize import curve_fit

def sigmoid(x,a,b,c):
    y=c/(1+np.exp(a*x+b))
    return y

#xdata=np.array([1,2,3,4,5, 6, 7, 8, 9, 10])
#ydata=np.array([2,3,5,4,10,16,23,31,36,34])

if __name__=='__main__':
    #获得参数
    list_str=[]
    num=[]
    for i in range(1,len(sys.argv)):
        list_str.append(sys.argv[i].replace(",",""))
    list_str[0]=list_str[0].replace("[","")
    list_str[len(sys.argv)-2]=list_str[len(sys.argv)-2].replace("]","")
    list_y=list(map(float,list_str))
    #print('list_y:',list_y)
    #获得x
    list_x=[]
    for i in range(0,len(list_y)):
        list_x.append(i+1)
    #print('list_x:',list_x)
    #进行拟合
    xdata=np.array(list_x)
    ydata=np.array(list_y)
    popt,pcov=curve_fit(sigmoid,xdata,ydata,bounds=([-1.,-1.,-np.inf],[10.,10.,np.inf]))
    #print('函数y=',popt[2],'/(1+e^(',popt[0],'*(x+',popt[1],')))')
    print(popt[0],popt[1],popt[2])


#拟合y值
#x=np.array([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20])
#yvals=sigmoid(x,*popt)
#print('函数y=',c,'/(1+e^(',a,'*(x+',b,')))')
#print('yvals:',yvals)

#绘图
#plot1=plt.plot(xdata,ydata,'s',label='origin values')
#plot2=plt.plot(x,yvals,'r',label='polyfit values')
#plt.xlabel('x')
#plt.ylabel('y')
#plt.legend(loc=4)
#plt.title('curve_fit')
#plt.show()





