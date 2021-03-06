package com.wanliu.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wanliu.admin.entity.ReturnResult;
import com.wanliu.admin.enu.ReturnResultStatus;
import com.wanliu.admin.mapper.BillMapper;
import com.wanliu.admin.mapper.UserMapper;
import com.wanliu.admin.pojo.Bill;
import com.wanliu.admin.pojo.User;
import com.wanliu.admin.service.UserService;
import com.wanliu.admin.utils.HexUtils;
import com.wanliu.admin.utils.Sout;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BillMapper billMapper;

    @Autowired
    Web3j web3j;

    /**
     * ??????
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ReturnResult register(String username,
                                 String password
    ) throws IOException {
        /**
         * ????????????
         */
        Sout.splService("username", username);
        Sout.splService("password", password);

        /**
         * ??????????????????????????????????????????
         */
        User user = new User();
        user.setUsername(username);
        List<User> UserSqlList = userMapper.select(user);
        if (UserSqlList != null && UserSqlList.size() > 0) {
            return new ReturnResult(ReturnResultStatus.FAIL, ReturnResultStatus.FAIL_MESSAGE);
        }

        /**
         * ????????????
         */
        Credentials account = createAccount(password);
        if (account == null) {
            return new ReturnResult(ReturnResultStatus.FAIL, ReturnResultStatus.FAIL_MESSAGE);
        }
        String address = account.getAddress();
        String privateKey = account.getEcKeyPair().getPrivateKey().toString(16);
        /**
         * ???????????????
         */
        user.setPassword(password);
        user.setGethAddress(address);
        user.setMoney(new BigDecimal(0));
        user.setPrivateKey(privateKey);
        int insert = userMapper.insert(user);
        return new ReturnResult(ReturnResultStatus.SUCCESS, ReturnResultStatus.SUCCESS_MESSAGE);
    }

    /**
     * ????????????
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public ReturnResult login(String username,
                              String password,
                              HttpServletRequest request,
                              HttpServletResponse response
    ) {
        /**
         * ????????????
         */
        Sout.splService("username", username);
        Sout.splService("password", password);

        /**
         * ???????????????????????????
         */
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        List<User> UserSqlList = userMapper.select(user);

        if (UserSqlList == null || UserSqlList.size() <= 0) {
            return new ReturnResult(ReturnResultStatus.FAIL, ReturnResultStatus.FAIL_MESSAGE);
        }

        /**
         * ??????session cookie
         */
        HttpSession session = request.getSession();
        session.setAttribute("user", UserSqlList.get(0));
        Cookie cookie1 = new Cookie("username", UserSqlList.get(0).getUsername());
        cookie1.setPath("/");
        response.addCookie(cookie1);
        return new ReturnResult(ReturnResultStatus.SUCCESS, ReturnResultStatus.SUCCESS_MESSAGE);
    }

    /**
     * ????????????
     *
     * @param aoData
     * @param request
     * @return
     */
    @Override
    public String findBillAll(String aoData,
                              HttpServletRequest request
    ) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        /**
         * ???????????????
         */
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return JSON.toJSONString(new ReturnResult<>(ReturnResultStatus.FAIL, ReturnResultStatus.FAIL_MESSAGE));
        }

        /**
         * ????????????
         */
        Sout.splService("aoData", aoData);
        JSONArray jsonarray = JSONArray.fromObject(aoData);
        int sEcho = 0;
        int startPage = 0; // ????????????
        int rowsPage = 10; // ?????????????????????

        for (int i = 0; i < jsonarray.size(); i++) {
            net.sf.json.JSONObject obj = (net.sf.json.JSONObject) jsonarray.get(i);
            //????????????
            if (obj.get("name").equals("sEcho")) {
                sEcho = obj.getInt("value");
            }
            //???????????????
            if (obj.get("name").equals("iDisplayStart")) {
                startPage = obj.getInt("value");
            }
            //??????????????????????????????
            if (obj.get("name").equals("iDisplayLength")) {
                rowsPage = obj.getInt("value");
            }
        }

        /**
         * ????????????
         */
        Bill bill = new Bill();
        bill.setUsername(user.getUsername());
        Page<Object> page = null;
        if (rowsPage != -1) {
            page = PageHelper.startPage(startPage / rowsPage + 1, rowsPage);
        }
        List<Bill> BillList = billMapper.select(bill);

        /**
         * ???????????????
         */
        List<String[]> tr = new ArrayList<String[]>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Bill billSql : BillList) {
            //?????????????????????????????????
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(billSql.getAddress()).send();
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(billSql.getAddress()).send();

            String data = "";
            if (ethGetTransactionReceipt.getTransactionReceipt().isPresent()) {
                //??????inputData hash????????????
                String input = ethTransaction.getTransaction().get().getInput();
                String strData = HexUtils.toStringHex(input.substring(2));
                Sout.splService("data", strData);
                data = strData;
            } else {
                data = billSql.getAddress();
            }
            String[] td = {
                    billSql.getId() + "",
                    billSql.getUsername(),
                    billSql.getOtherUsername(),
                    data,
                    billSql.getOutOrIn() + "",
                    billSql.getCreateTime() == null ? "" : sdf.format(billSql.getCreateTime())
            };
            tr.add(td);
        }
        long total = 0;
        if (page != null) {
            total = page.getTotal();
        } else {
            total = BillList.size();
        }
        net.sf.json.JSONObject getObj = new net.sf.json.JSONObject();
        getObj.put("sEcho", sEcho);// ???????????????
        getObj.put("iTotalRecords", total);//???????????????
        getObj.put("iTotalDisplayRecords", total);//???????????????,??????????????????????????????
        getObj.put("aaData", tr);//??????JSON????????????
        return getObj.toString();
    }

    /**
     * ??????????????????
     *
     * @param request
     * @return
     */
    @Override
    public ReturnResult findMoneyById(HttpServletRequest request) {
        /**
         * ???????????????
         */
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new ReturnResult<>(ReturnResultStatus.FAIL, ReturnResultStatus.FAIL_MESSAGE);
        }

        User userSql = userMapper.selectByPrimaryKey(user.getId());
        return new ReturnResult<>(ReturnResultStatus.SUCCESS, ReturnResultStatus.SUCCESS, userSql.getMoney());
    }

    /**
     * ??????
     *
     * @param otherUsername
     * @param outMoney
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnResult transfer(String otherUsername, BigDecimal outMoney, HttpServletRequest request) throws Exception {
        /**
         * ???????????????
         */
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return new ReturnResult<>(ReturnResultStatus.FAIL, ReturnResultStatus.FAIL_MESSAGE);
        }

        /**
         * ???????????????????????????????????????
         */
        User userSelect = new User();
        userSelect.setUsername(otherUsername);
        List<User> otherUserSqlList = userMapper.select(userSelect);
        if (otherUserSqlList == null || otherUserSqlList.size() <= 0) {
            return new ReturnResult<>(ReturnResultStatus.ISNULL, "???????????????");
        }

        /**
         * ??????
         */
        User userSql = userMapper.selectByPrimaryKey(user.getId());
        userSql.setMoney(userSql.getMoney().subtract(outMoney));
        if (userSql.getMoney().doubleValue() < 0) {
            return new ReturnResult<>(ReturnResultStatus.FAIL, "????????????");
        }
        userMapper.updateByPrimaryKeySelective(userSql);

        User otherUserSql = otherUserSqlList.get(0);
        otherUserSql.setMoney(otherUserSql.getMoney().add(outMoney));
        userMapper.updateByPrimaryKeySelective(otherUserSql);

        /**
         * ???????????????
         */
        //???????????????nonce
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(user.getGethAddress(), DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = transactionCount.getTransactionCount();
        Sout.splService("nonce", nonce);
        BigInteger gasLimit = Contract.GAS_LIMIT.divide(new BigInteger("1"));
        Sout.splService("gasLimit", gasLimit);
        //??????RawTransaction?????????
        //??????????????????
        String hashData = HexUtils.toHexString(outMoney.toString());
        Sout.splService("hashData", hashData);

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce, BigInteger.valueOf(0), gasLimit, otherUserSql.getGethAddress(), BigInteger.valueOf(new Integer(0)), hashData);

        //????????????????????????
        Credentials creds = Credentials.create(user.getPrivateKey());
        //????????????????????????
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, creds);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();

//        EthSendTransaction send = web3j.ethSendRawTransaction().send();
//        String transactionHash = send.getTransactionHash();

        Sout.splService("transactionHash", ethSendTransaction.getTransactionHash());

        String transactionHash = ethSendTransaction.getTransactionHash();

        //???????????????????????????
        if (ethSendTransaction.getError() != null) {
            Response.Error error = ethSendTransaction.getError();
            System.out.println(error.getMessage());
        }

        if (transactionHash == null) {
            //????????????
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ReturnResult<>(ReturnResultStatus.ISNULL, "????????????");
        }

//        EthTransaction send = web3j.ethGetTransactionByHash(transactionHash).send();
        //???????????????
        EthGetTransactionReceipt send1 = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
        Sout.splService("????????????", send1.getTransactionReceipt().isPresent());

//        if (!send1.getTransactionReceipt().isPresent()) {
//            //?????????????????????
//        }
        //????????? ?????????
//        Thread.sleep(4000);
        //?????????????????????
        int num = 0;
        while(!send1.getTransactionReceipt().isPresent() && num < 50){
            num ++;
            Thread.sleep(200);
        }

        /**
         * ???????????????
         */
        Date date = new Date();
        Bill bill = new Bill();
        bill.setUsername(user.getUsername());
        bill.setAddress(transactionHash);
        bill.setCreateTime(date);
        bill.setOtherUsername(otherUserSql.getUsername());
        bill.setOutOrIn(1);
        billMapper.insert(bill);

        bill.setId(null);
        bill.setUsername(otherUserSql.getUsername());
        bill.setOtherUsername(user.getUsername());
        bill.setAddress(transactionHash);
        bill.setCreateTime(date);
        bill.setOutOrIn(2);
        billMapper.insert(bill);

        //???????????????
        return new ReturnResult(ReturnResultStatus.SUCCESS, ReturnResultStatus.SUCCESS_MESSAGE);
    }

    /**
     * ?????????????????????
     *
     * @param password
     * @return
     */
    public Credentials createAccount(String password) {
        String filePath = "D:\\1\\Pictures";
        String fileName = null;
        try {
//            String path = request.getServletContext().getRealPath("/");
            fileName = WalletUtils.generateNewWalletFile(password, new File(filePath), false);
            Credentials credentials = WalletUtils.loadCredentials(password, filePath + "/" + fileName);
            return credentials;
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
