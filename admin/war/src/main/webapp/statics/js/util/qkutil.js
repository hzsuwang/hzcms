/**
 * Created by jhj on 17-5-4.
 */


var Qk = function () {

    /**
     * 封装post请求
     * @param url  url地址
     * @param param js对象
     * @param successCallback 成功
     * @param errorCallback　出错
     * @param completeCallback
     */
    function post(url, param, successCallback, errorCallback, completeCallback) {
        if (!url) {
            console.log("url can't be  null");
            return;
        }

        if (successCallback && typeof successCallback !== 'function') {
            console.log("successCallback is not a function");
            return;
        }

        if (errorCallback && typeof errorCallback !== 'function') {
            console.log("errorCallback is not a function");
            return;
        }

        if (completeCallback && typeof completeCallback !== 'function') {
            console.log("completeCallback is not a function");
            return;
        }

        $.ajax({
            type: 'POST',
            url: url,
            data: param || {},
            dataType: 'json',
            success: function (data) {
                if (data.success) {
                    if (successCallback) {
                        successCallback(data);
                    } else {
                        window.location.reload();
                    }
                } else {
                    if (errorCallback) {
                        errorCallback(data);
                    } else {
                        TINY.box.show({html: data.errorMessage || data.eMsg || "操作失败" , animate: false, close: false, boxid: 'error', top: 250});
                    }
                }
            },
            complete: function (xhr, status) {
                if (completeCallback) {
                    completeCallback();
                }
                /*returnLogin(xhr,status);*/
            }
        });
    }

    /**
     * 弹出确认对话框
     * @param html :显示的文字内容
     * @param option : 配置项
     * @param yes: 确定函数
     * @param no :取消函数
     */
    function confirm(html, option, yes, no) {

        if (yes && typeof yes === 'function'){
            Qk.ok = yes;
        }

        if (no && typeof no === 'function'){
            Qk.no = no;
        }
        if (option){
            Qk.confirmOpt = option;
        } else {
            Qk.confirmOpt = null;
        }
        var text =
            '<div id="js-dialog-confirm" style="">' +
                '<div class="filterDiv">' +
                    '<div class="jumpDiv" ></div>' +
                    '<div class="contentDiv" style="overflow:auto;background:#fff;top:15%;left:30%;width:500px; height:150px;">' +
                        '<form>' +
                            '<h2 style="text-align:center;padding:10px;">提示</h2>' +
                            '<div id="Info-add">' +
                                '<table width="100%" border="0" cellspacing="0" cellpadding="0" >' +
                                    '<tbody>' +
                                        '<tr>' +
                                            '<br>' +
                                            '<h3 style="text-align:center">'+ html +'</h3>' +
                                        '</tr>' +

                                    '</tbody>' +
                                    '<tfoot>' +
                                        '<tr>' +
                                            '<td style="text-align: center">' +
                                            '<input type="button"  onclick="Qk.ok()" value=" 确定 " class="button"/>     ' +
                                            '<input type="reset"   onclick="Qk.no()" value=" 取消 " class="button"/>   ' +
                                            '</td>'+
                                        '</tr>'+
                                    '</tfoot>'+
                                '</table>'+
                            '</div>'+
                        '</form>'+
                    '</div>'+
                '</div>'+
            '</div>';

        $('body').append(text);
    }
    /**
     * 弹出确认对话框
     * @param html :显示的文字内容
     * @param option : 配置项
     * @param yes: 确定函数
     * @param no :取消函数
     */
    function popup(html, option, yes, no) {

        if (yes && typeof yes === 'function'){
            Qk.ok = yes;
        }

        if (no && typeof no === 'function'){
            Qk.no = no;
        }
        if (option){
            Qk.confirmOpt = option;
        } else {
            Qk.confirmOpt = null;
        }
        var style = 'width:auto; height:auto;';
        if (option.style) {
            style = option.style;
        }
        var text =
            '<div id="js-dialog-confirm" style="">' +
                '<div class="filterDiv">' +
                    '<div class="jumpDiv" ></div>' +
                    '<div class="contentDiv" style="overflow:auto;background:#fff;top:15%;left:30%;'+style+'">' +
                        '<form>' +
                            '<h2 style="text-align:center;padding:10px;">提示</h2>' +
                            '<div id="Info-add">' +
                                  html+
                                  '<div style="text-align: center"> <input type="reset"  style="text-align: center" onclick="Qk.no()" value="关闭" class="button"/>  </div> ' +
                             '</div>'+
                        '</form>'+
                    '</div>'+
                '</div>'+
            '</div>';

        $('body').append(text);
    }

    var ok = function () {
        console.log('ok');
    };
    var no = function () {
        $('#js-dialog-confirm').remove();
        console.log('no');
    };
    var confirmOpt = {};

    return {
        post: post,
        confirm: confirm,
        popup: popup,
        ok:ok,
        no:no,
        confirmOpt: confirmOpt
    }
}();