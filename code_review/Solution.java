import java.math.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;

public class Solution {
    public static void main(String... args) {
        JUnitCore.main("Solution");
    }

    @Test
    public void testSolution() throws Exception {
        // Create a cart.
        Cart cart = new Cart("GBP");

        // Create an item and add it to the cart.
        // This card reader costs £59 before a £25 discount.
        BigDecimal price = new BigDecimal("59.00");
        Discount discount = new Discount(new BigDecimal("25.00"), null);
        cart.items.add(new Item("Card reader", price, "GBP", discount));

        // Add a 10% discount to the cart.
        cart.discount = new Discount("10% off!", null, new BigDecimal("0.1"));
      
        // Compute the cart's total.
        BigDecimal total = Util.calculateAmountToPayForCart(cart);

        // Assert that the cart's total is correctly computed.
        Assert.assertEquals(new BigDecimal("53.10"), total);
    }
}

class Item {
    public String name;
    public BigDecimal price;
    public String currency;
    public Discount discount;

    public Item(String name, BigDecimal price, String currency, Discount discount) {
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.discount = discount;
    }
}

class Discount {
    public String name;
    public BigDecimal amount;
    public BigDecimal percentage;

    public Discount(BigDecimal amount, BigDecimal percentage) {
        this(null, amount, percentage);
    }

    public Discount(String name, BigDecimal amount, BigDecimal percentage) {
        this.name = name;
        this.amount = amount;
        this.percentage = percentage;

        if (amount == null) {
            if (percentage == null) {
                throw new IllegalArgumentException("Can't have percentage and money discount at the same time.");
            }
        } else {
            if (percentage != null) {
                throw new IllegalArgumentException("Can't have percentage and money discount at the same time.");
            }
        }
    }
}

class Cart {
    public String currency;
    public List<Item> items;
    public Discount discount;

    public Cart(String currency) {
        this(currency, null);
    }

    public Cart(String currency, List<Item> items) {
        this.currency = currency;
        this.items = items != null ? items : new ArrayList<>();

        for (Item item : this.items) {
            if (!item.currency.equals(currency)) {
                throw new IllegalArgumentException();
            }
        }
    }
}

class Util {
    static BigDecimal calculateAmountToPayForCart(Cart cart) {
        BigDecimal amountToPay = BigDecimal.ZERO;

        for (Item item : cart.items) {
            amountToPay = amountToPay.add(item.price);
        }

        amountToPay = applyDiscount(cart.discount, amountToPay);

        return amountToPay.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal applyDiscount(Discount discount, BigDecimal amount) {
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (discount.percentage != null) {
            discountAmount = amount.multiply(discount.percentage);
        }

        if (discount.amount != null) {
            discountAmount = amount;
        }

        return amount.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }
}
