<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme()+"://"+
            request.getServerName()+":"+request.getServerPort()+
            request.getContextPath()+"/";


%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script>
        $(function () {
            get();
        })

        function get() {
                $.ajax({
                    url:"workbench/transaction/get.do",
                    data:{},
                    type:"get",
                    dataType:"json",
                    success:function (data) {
                        console.log(data)
                        console.log(data[0].name)
                        console.log(data.name)
                        console.log(data.length)
                        var index = [];
                        for (var i = 0; i < data.length; i++) {
                           // index += "'"+data[i].name+"'";
                           //
                           // if (i < data.length-1){
                           //     index += ",";
                           // }
                            index.push(data[i].name)
                        }
                        console.log(index)
                        console.log( ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎'])

                        var myChart = echarts.init(document.getElementById('main'));
                        option = {
                            tooltip: {
                                trigger: 'item',
                                formatter: '{a} <br/>{b}: {c} ({d}%)'
                            },
                            legend: {
                                orient: 'vertical',
                                left: 10,
                                data: index
                            },
                            series: [
                                {
                                    name: '访问来源',
                                    type: 'pie',
                                    radius: ['50%', '70%'],
                                    avoidLabelOverlap: false,
                                    label: {
                                        show: false,
                                        position: 'center'
                                    },
                                    emphasis: {
                                        label: {
                                            show: true,
                                            fontSize: '30',
                                            fontWeight: 'bold'
                                        }
                                    },
                                    labelLine: {
                                        show: false
                                    },
                                    data:data
                                }
                            ]
                        };
                        myChart.setOption(option);
                    }
                })
        }

    </script>


</head>
<body>
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
