import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;

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
		Discount discount = new AmountDiscount(new BigDecimal("25.00"));
		cart.items.add(new Item("Card reader", price, "GBP", discount));

		// Add a 10% discount to the cart.
		cart.discount = new PercentageDiscount("10% off!", new BigDecimal("0.1"));

		// Compute the cart's total.
		BigDecimal total = cart.getTotalAmount();

		// Assert that the cart's total is correctly computed.
		Assert.assertEquals(new BigDecimal("30.60"), total);
	}

	public interface Discount {
		BigDecimal getDiscount(BigDecimal price);
	}

	public class PercentageDiscount implements Discount {
		public String name;
		public BigDecimal percentage;

		public PercentageDiscount(BigDecimal percentage) {
			this(null, percentage);
		}

		public PercentageDiscount(String name, BigDecimal percentage) {
			this.name = name;
			this.percentage = percentage;

			if (percentage == null) {
				throw new IllegalArgumentException("A percentage discount cant be null.");
			}
		}

		public BigDecimal getDiscount(BigDecimal price) {
			BigDecimal discountAmount = BigDecimal.ZERO;

			if (percentage != null) {
				discountAmount = price.multiply(percentage);
			}
			return discountAmount.setScale(2, RoundingMode.HALF_UP);
		}

	}

	public class AmountDiscount implements Discount {
		public String name;
		public BigDecimal amount;

		public AmountDiscount(BigDecimal amount) {
			this(null, amount);
		}

		public AmountDiscount(String name, BigDecimal amount) {
			this.name = name;
			this.amount = amount;
			if (amount == null) {
				throw new IllegalArgumentException("Can't have percentage and money discount at the same time.");
			}
		}

		public BigDecimal getDiscount(BigDecimal price) {
			BigDecimal discountAmount = BigDecimal.ZERO;
			if (amount != null) {
				discountAmount = amount;
			}

			return discountAmount.setScale(2, RoundingMode.HALF_UP);
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

		public BigDecimal getPrice() {
			if (discount != null) {
				return price.subtract(discount.getDiscount(price));
			}
			return price;
		}

	}

	public class Cart {
		public String currency;
		public final List<Item> items;
		public Discount discount;

		public Cart(String currency) {
			this(currency, new ArrayList<Item>());
		}

		public Cart(String currency, List<Item> items) {
			this.currency = currency;
			this.items = items;
			if (items == null) {
				throw new IllegalArgumentException();
			}
			for (Item item : this.items) {
				if (!item.currency.equals(currency)) {
					throw new IllegalArgumentException();
				}
			}
		}

		public BigDecimal getTotalAmount() {
			BigDecimal amountToPay = BigDecimal.ZERO;

			for (Item item : items) {
				amountToPay = amountToPay.add(item.getPrice());
			}

			if (discount != null) {
				BigDecimal discountAmount = discount.getDiscount(amountToPay);
				amountToPay = amountToPay.subtract(discountAmount);
			}

			return amountToPay.setScale(2, RoundingMode.HALF_UP);
		}

	}
}
