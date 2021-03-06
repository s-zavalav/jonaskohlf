解决传递中文问题
以GET形式提交表单数据时，数据会被包含在URL的抽象路径中"?"右侧.而抽象路径部分会随着请求的
请求行传递给服务端(请求行的第二部分)。
HTTP协议要求请求的请求行和消息头是文本部分，且符合的字符集必须是ISO8859，这是一个欧洲编码
里面不支持中文!

解决办法:
用ISO8859支持的字符来表示其不支持的字符
字符'0'和字符'1'是ISO8859支持的字符。我们用这两个字符的组合表示2进制中的1010的字节组合。
例如:
希望传递"范传奇"时。
浏览器会进行如下操作:
1:浏览器会利用支持中文的字符集(通常是UTF-8。准确的说是用页面指定的字符集，<meta charset="">)
  将中文转换为对应的一组字节:"范传奇:----UTF-8---->11101000.....(9个字节，72个2进制)
2:将2进制中的1和0用字符'1'和字符'0'表示，既可以传递了
  原本的:username=范传奇&password=123456
  变为:username=111010001......(72个字符)&password=123456

虽然解决了问题，但是副作用:数据太长，影响传输速度

进而优化长度问题
解决办法:
将2进制的1和0的组合用16进制表示。因为16进制使用的字符可以用(0-9A-F表示，它们仍然是ISO8859
支持的字符)

换算关系
2进制         10进制            16进制
0000            0               0
0001            1               1
0010            2               2
0011            3               3
0100            4               4
0101            5               5
0110            6               6
0111            7               7
1000            8               8
1001            9               9
1010            10              A
1011            11              B
1100            12              C
1101            13              D
1110            14              E
1111            15              F

1个字节用2进制需要8位，而用16进制则仅需要2位
例如:
11101000
E8

将2进制部分换位16进制部分后，传递中文时可以变为:
原本:username=111010001......(72个字符)&password=123456
变为:username=E88C83...(18个字符)&password=123456

虽然解决了问题，但是副作用:服务端如何与实际的英文数字内容区分?
假如该用户输入的用户名就叫:E88C83
username=E88C83&password=123456

服务端读取到E88C83如何理解?
1:该人名字就叫这个?
2:6个16进制，表示3个字节，还原为中文"范"?

为了解决这个问题，URL规定，若使用英文和数字表示的是16进制，那必须在每两位16进制前加上一个%
因此:
username=%E8%8C%83&password=123456
服务端则认为用户名是3个字节
如果
username=E88C83&password=123456
服务端则认为用户名就是E88C83

范---->UTF-8---->2进制:11101000 10001100 10000011----换算为16进制---->E8 8C 83
传递是每两个16进制前加上%就形成了:username=%E8%8C%83&password=123456

服务端需要反向还原，但是此工作java已经提供了API：URLDecoder.decode()

实现:
修改HttpServletRequest的parseParameter方法，将参数部分转化后再进行解析



