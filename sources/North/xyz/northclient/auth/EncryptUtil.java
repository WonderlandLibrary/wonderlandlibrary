package xyz.northclient.auth;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

public class EncryptUtil {
    public String[] chinese;

    public EncryptUtil() {
        String a  ="的 一 是 了 我 不 人 在 他 有 这 个 上 们 来 到 时 大 地 为 子 中 你 说 生 国 年 着 就 那 和 要 她 出 也 得 里 后 自 以 会 家 可 下 而 过 天 去 能 对 小 多 然 于 心 学 么 之 都 好 看 起 发 当 没 成 只 如 事 把 还 用 第 样 道 想 作 种 开 美 总 从 无 情 己 面 最 女 但 现 前 些 所 同 日 手 又 行 意 动 方 期 它 头 经 \n" +
                "长 儿 回 位 分 爱 老 因 很 给 名 法 间 斯 知 世 什 两 次 使 身 者 被 高 已 亲 其 进 此 话 常 与 活 正 感 见 明 问 力 理 尔 点 文 几 定 本 公 特 做 外 孩 相 西 果 走 将 月 十 实 向 声 车 全 信 重 三 机 工 物 气 每 并 别 真 打 太 新 比 才 便 夫 再 书 部 水 像 眼 等 体 却 加 电 主 界 门 利 海 受 听 表 德 少 克 代 员  \n" +
                "许 先 口 由 死 安 写 性 马 光 白 或 住 难 望 教 命 花 结 乐 色 更 拉 东 神 记 处 让 母 父 应 直 字 场 平 报 友 关 放 至 张 认 接 告 入 笑 内 英 军 候 民 岁 往 何 度 山 觉 路 带 万 男 边 风 解 叫 任 金 快 原 吃 妈 变 通 师 立 象 数 四 失 满 战 远 格 士 音 轻 目 条 呢 病 始 达 深 完 今 提 求 清 王 化 空 业 思 切 怎  \n" +
                "非 找 片 罗 钱 吗 语 元 喜 曾 离 飞 科 言 干 流 欢 约 各 即 指 合 反 题 必 该 论 交 终 林 请 医 晚 制 球 决 传 画 保 读 运 及 则 房 早 院 量 苦 火 布 品 近 坐 产 答 星 精 视 五 连 司 巴 奇 管 类 未 朋 且 婚 台 夜 青 北 队 久 乎 越 观 落 尽 形 影 红 爸 百 令 周 吧 识 步 希 亚 术 留 市 半 热 送 兴 造 谈 容 极 随 演  \n" +
                "收 首 根 讲 整 式 取 照 办 强 石 古 华 拿 计 您 装 似 足 双 妻 尼 转 诉 米 称 丽 客 南 领 节 衣 站 黑 刻 统 断 福 城 故 历 惊 脸 选 包 紧 争 另 建 维 绝 树 系 伤 示 愿 持 千 史 谁 准 联 妇 纪 基 买 志 静 阿 诗 独 复 痛 消 社 算 义 竟 确 酒";

        chinese = a.split(" ");
    }

    public static String encrypt(String plainText, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public static PublicKey getPublicKeyFromString(String publicKeyBase64) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrivateKeyFromString(String privateKeyBase64) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }
    public static String encrypt(String toCrypt) {
        try {

            return encrypt(toCrypt, getPublicKeyFromString("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmC98hSppWBSWZwjenU6AeSxHgjm9IPd7xASZem5ehMIi2WOc3hEv3PAqRwpokIyfw8rE3cRrl0+SMq+xinYjzIgeJYp+H3eGMikafHSoQoD9V/kTxVaDcWu9SVKaBgfoQ0PnSUkCg4K7gqvHDLXIq9bUQ85jjAgodyrUlsm1Pk3PDI0+n1TmkMZP0MxOR5GLyHXFL9OWsVeHb9w7SsQQrp+r05lu7svZqlAN2zGTuEFSZNfXkETCKSB0v0BYmAyQvtT457dhS2/8m4ZH/qruRO5hDrswCJRy8vi1m77waJImJgFkxZmj4SdbyInwZh9eQ/AsHFKyZK38AjD63tNWdwIDAQAB"));
        } catch (Exception e) {

        }
        return null;
        }

        public static String decrypt(String toCrypt) {

            try {
               return decrypt(toCrypt, getPrivateKeyFromString("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCH64MdeInPHgOuSvYh4PgKrx1q7zCAWnMlKW/sKYPVCqPVOTOhpT6vd6y+Jwk6tlA1jHnUxhDtJtP5bAbJdTXfzbGGjOW/qgNHlBqQhIkMjdjtxIEmRktI6husJV+3Tzb+SHyy4/MByu70qs8iL5irONmzZHyqonjqC3c/gQhHueG1EfP+tSxxFYfpIiGoCanraea7vbikCpmcBwF02mniFW6AnMAr7R1TPxSaIOPV94FGj/MLNtcF7ijLlKTcX/lpZywHVOsBWt/U2pdfShdKvIdq84rHf8P1MTrx6MYj+WEBUVd2CV0Lx/jXkoZHIJPPnQa9fpTsmCo41NCye7xdAgMBAAECggEAacvDrvjYK8MtEkl+9DfsWkScHVQ46G9/GPn8CFL4LqethpaTvcql1GbOquQvpuep30SMwDW7jxyd+pM/ZSg62w18lUKuwlVPYDPDyaGgG4l9xPUjoestRoATMheVPVYHdD5v8b7EtCyFeyRJPk7WAtCpg50//IGipGpwU5nYHBtJhQEKHsVW4ez78sFFJ1brsJjdI2laKhuF8UwQ9Ri7m/r1HNMqzEcR6WR4UZMHsegDEOQBJLxRLFnE3/4PsL9eOztrhvidb+cbyvsSfRt2xKs+Zg7WGDLe0eGuJ5bfvuV9k+Xg+Eyv8VJMnUJ8Swc9A6cIMPk8dgq0qXDYm97ccQKBgQDbS42svo8tY/pjtvWKCef6S1+gK7kcOCb/b06510ZZyp3Pvb/ErSMQyL+eaEegT3OZVMUQVupmM3B6GncOKQaefAKm2Ni3iWdzVUpzMuc8vNqV3XbClekc5ox0IYc9K5p3KjbMjq0q+dk9qLAIMaZft/Zwu4lOIAafkyJDzHhRpwKBgQCeq3dQqoWPFW/xgt1IEAJlX/tutWcf95f22dNOxqkoIuqd8HL+v//V5ebQCM0c4P0qmtFb8h5IHAGUoJmqdYbY6rwRYmpF7wuiXZqh6ghh1I1RRxUuSx4N/cyfbEnPrlUs4SSs6vHslbmqqilx/e7pbHtjXJVv/nSzwenc/S5aWwKBgQC1LRGqs3FVHfGjamgoGJEYoCcsDJON9fJ6PPq83VeZHaYOGQWTtztuRXzSofLzRlEKUBfTrQtRTpRRvQW5pDl8vJXwRN2fnRzkE4ZD8L1M1/Z7tJHGp9YFc5B87apu5lRiuXS2wfXduLg7kd+FlYeQM0uezcHd0uXF9uhrPUX2pwKBgEEHDtg9eo1AgOde9adyADlh0970c3Yzd9FQRnyziGINpU/jWo2zTHtyRF0gLkfWoYsTdhgTq3tsFu29wByPHBvMYBVT2zAcHw2FBTm0e4j0npIf6rtVR9T+Hh18OOfnj7rb7rb7dvi6CCS8pk20f3Sk5BC9wfTrORLutxS9ooCfAoGAT/ZIr/vphGpWd2VGyI6quEWtTSWis22TNxqK0CX/qsYDJsi9Y5bbRtLyHGeAGXwpXGLmIsbPFwwi5IlabiTwIjtINl5mQBxmRHgFYg5/IOtRgdjaRrNbPkqIr90UjWuz9YL9ChvLUdTjnG3b237WthPXso5Y4pbd+L3ABi2w2v4="));
            } catch (Exception e) {

            }
            return null;
        }
 /*   public static String encrypt(String toCrypt) {
        int maxRandomization = 140;
        StringBuilder crypted = new StringBuilder();
        for(char c : toCrypt.toCharArray()) {
            int random = new Random().nextInt(maxRandomization)+90;
            int moved = c+random-90;
            char ca = (char) moved;
            crypted.append(ca);
            crypted.append((char)random);
        }
        return Base64.getEncoder().encodeToString(crypted.toString().getBytes());
    }

    public static String decrypt(String toDecrypt) {
        toDecrypt = new String(Base64.getDecoder().decode(toDecrypt));
        StringBuilder decrypted = new StringBuilder();
        boolean sum = false;
        char toCharDecrypt = ' ';
        for(char c : toDecrypt.toCharArray()) {
            if(sum) {
                char sumChar = c;
                int sumCharInt = sumChar;
                sumCharInt -= 90;

                int toDecryptCharInt = toCharDecrypt;
                toDecryptCharInt -= sumCharInt;
                char dec = (char) toDecryptCharInt;
                decrypted.append(dec);
                sum = false;
            } else {
                toCharDecrypt = c;
                sum = true;
            }
        }
        return decrypted.toString();
    }*/
}
