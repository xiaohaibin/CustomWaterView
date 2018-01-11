## 自定义仿支付宝蚂蚁森林水滴控件（完善中...）

 ![ABC](https://github.com/xiaohaibin/CustomWaterView/blob/master/screenshot/gif.gif) 


## 已实现功能

### 1、自定义小圆球

### 2、圆球内文字显示

### 3、圆球上下抖动

### 4、圆球淡入淡出

## To Do List

### 除去中间树的控件所占的区域，水滴位置随机生成（随机生成算法）

#### 计划通过设置left和top或者设置leftmargin,topmargin也行，实现绝对定位组件不重叠需要自己记录下来一个rect集合，在添加的时候进行判断添加的view是否会重叠


圆点坐标：(x0,y0) 
半径：r 
角度：a0 

则圆上任一点为：（x1,y1） 
x1   =   x0   +   r   *   cos(ao   *   3.14   /180   ) 
y1   =   y0   +   r   *   sin(ao   *   3.14   /180   ) 