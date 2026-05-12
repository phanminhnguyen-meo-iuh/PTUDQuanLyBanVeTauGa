package com.mycompany.prj1.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/**
 * Button cho seat picker - tự vẽ background VÀ text.
 * Không phụ thuộc FlatLaf - màu sắc luôn được giữ nguyên dù enabled hay disabled.
 */
public class GheButton extends JButton {

    public enum TrangThai {
        TRONG,        // ghế trống - xám
        DANG_CHON,    // đang được chọn - xanh dương
        DANG_GIU,     // đang giữ chỗ - cam
        DA_BAN        // đã bán - đỏ
    }

    private TrangThai trangThai = TrangThai.TRONG;

    public GheButton(String text) {
        super(text);
        // Tắt mọi style mặc định để paintComponent tự vẽ
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    public void setTrangThai(TrangThai tt) {
        this.trangThai = tt;
        repaint();
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    /** Màu nền theo trạng thái */
    private Color layMauNen() {
        switch (trangThai) {
            case DA_BAN:    return new Color(220, 53, 69);    // đỏ
            case DANG_GIU:  return new Color(255, 153, 0);    // cam
            case DANG_CHON: return new Color(0, 120, 215);    // xanh dương
            case TRONG:
            default:        return new Color(220, 220, 220);  // xám
        }
    }

    /** Màu chữ theo trạng thái */
    private Color layMauChu() {
        switch (trangThai) {
            case DA_BAN:
            case DANG_GIU:
            case DANG_CHON:
                return Color.WHITE;            // 3 trạng thái nền đậm -> chữ trắng
            case TRONG:
            default:
                return new Color(33, 37, 41);  // ghế trống nền xám -> chữ đen
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // === 1. Vẽ background ===
        Color bg = layMauNen();

        // Hover effect cho ghế TRỐNG
        if (trangThai == TrangThai.TRONG && getModel().isRollover() && isEnabled()) {
            bg = new Color(190, 190, 190);
        }

        // Pressed effect
        if (isEnabled() && getModel().isPressed()) {
            bg = bg.darker();
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        // Border viền
        g2.setColor(bg.darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

        // === 2. Tự vẽ chữ (KHÔNG gọi super.paintComponent) ===
        // Tránh FlatLaf override màu chữ khi button disabled
        String text = getText();
        if (text != null && !text.isEmpty()) {
            g2.setFont(getFont());
            g2.setColor(layMauChu());

            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(text);
            int textH = fm.getAscent();

            // Canh giữa
            int x = (getWidth() - textW) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + textH;

            g2.drawString(text, x, y);
        }

        g2.dispose();
        // CỐ Ý không gọi super.paintComponent(g) -> FlatLaf không vẽ đè
    }
}