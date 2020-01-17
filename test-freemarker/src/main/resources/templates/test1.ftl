遍历数据模型中的list学生信息

<table border="1">
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>


    <#list stus as stu>
        <tr>
            <td>${stu_index + 1}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            <td>${stu.money}</td>
        </tr>
    </#list>
</table>

遍历map信息：
    <hr/>
    输出stu1的学生信息(数组形式)：<br/>
    姓名：${stuMap['stu1'].name}<br/>
    年龄：${stuMap['stu1'].age}<br/>

    <hr/>
    输出stu1的学生信息(对象形式)：<br/>
    姓名：${stuMap.stu1.name}<br/>
    年龄：${stuMap.stu1.age}<br/>

    <hr/>

    遍历输出两个学生信息：<br/>
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>钱包</td>
        </tr>
        <#list stuMap?keys as k>
            <tr>
                <td>${k_index + 1}</td>
                <td>${stuMap[k].name}</td>
                <td>${stuMap[k].age}</td>
                <td>${stuMap[k].money}</td>
            </tr>
        </#list>
    </table>
if指令
<#list stus as stu>
    <tr>
        <td>${stu_index + 1}</td>
        <td <#if stu.name =='小明'>style="background:red;"</#if>>${stu.name}</td>
        <td>${stu.age}</td>
        <td <#if (stu.money>300) >style="background:red;"</#if>>${stu.money}</td>
    </tr>
</#list>