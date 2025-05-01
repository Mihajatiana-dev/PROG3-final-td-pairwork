package org.Dish_Managament.modelDAO;

import org.Dish_Managament.database.DataSource;
import org.Dish_Managament.mapper.StockMapper;
import org.Dish_Managament.mapper.UnitMapper;
import org.Dish_Managament.model.FilterCriteria;
import org.Dish_Managament.model.Ingredient;
import org.Dish_Managament.model.OrderBy;
import org.Dish_Managament.model.OrderCriteria;
import org.Dish_Managament.model.StockInventory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO implements GenericDAO<Ingredient>{
    private final DataSource dataSource;
    private final UnitMapper unitMapper;
    private final StockMapper stockMapper;

    public IngredientDAO(DataSource dataSource, UnitMapper unitMapper, StockMapper stockMapper) {
        this.dataSource = dataSource;
        this.unitMapper = unitMapper;
        this.stockMapper = stockMapper;
    }

    public IngredientDAO() {
        this.dataSource = new DataSource();
        this.unitMapper = new UnitMapper();
        this.stockMapper= new StockMapper();
    }

    public List<StockInventory> getIngredientListOfStock(int idIngredient, LocalDateTime dateTime){
        List<StockInventory> stockInventories = new ArrayList<>();
        String query = "SELECT\n" +
                "si.id_stock_inventory,\n" +
                "i.name,\n" +
                "si.quantity,\n" +
                "si.unity,\n" +
                "si.stock,\n" +
                "si.stock_movement_date\n" +
                "FROM stock_inventory si\n" +
                "JOIN ingredient i ON si.id_ingredient = i.id_ingredient\n" +
                "WHERE i.id_ingredient = ?\n" +
                "AND si.stock_movement_date = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, idIngredient);
            statement.setTimestamp(2, Timestamp.valueOf(dateTime));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    StockInventory stockInventory = new StockInventory();
                    stockInventory.setStockInventoryId(resultSet.getInt("id_stock_inventory"));
                    stockInventory.setQuantity(resultSet.getDouble("quantity"));
                    stockInventory.setUnity(unitMapper.fromResultSetDbValue(resultSet.getString("unity")));
                    stockInventory.setStockMovement(stockMapper.fromResultSetDbValue(resultSet.getString("stock")));
                    stockInventory.setStockDateMovement(resultSet.getTimestamp("stock_movement_date").toLocalDateTime());

                    stockInventories.add(stockInventory);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stockInventories;
    }


    public List<Ingredient> FilteredPagedIngredientByCriteria
            (List<FilterCriteria> filterCriteriaList,
             List<OrderCriteria> orderCriteriaList,
             int page, int size)
    {
        List<Ingredient> filterIngredientList = new ArrayList<>();
        String sql = "SELECT ingredient.name, ingredient_price_history.ingredient_price,ingredient.unity, ingredient_price_history.last_modified\n" +
                "FROM ingredient\n" +
                "JOIN ingredient_price_history on ingredient.id_ingredient = ingredient_price_history.ingredient_id\n" +
                "WHERE 1=1"; // Keep the base WHERE clause to pass even if there is AND
        List<String> conditions = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }
        for(FilterCriteria filterCriteria : filterCriteriaList) {
            String column = filterCriteria.getColumn();
            Object value = filterCriteria.getValue();
            if ("name".equals(column)) {
                conditions.add("name ILIKE ?");
                values.add("%" + value + "%");
            }
            else if ("last_modified".equals(column)) {
                LocalDateTime[] updateDateRange = (LocalDateTime[]) value; //cast value into DATE
                values.add(Timestamp.valueOf(updateDateRange[0]));
                values.add(Timestamp.valueOf(updateDateRange[1]));
                conditions.add("last_modified BETWEEN ? AND ?");
            }
            else if ("ingredient_price".equals(column)){
                int [] ingredientPriceRage = (int []) value;
                if ((ingredientPriceRage[0] < ingredientPriceRage[1])) {
                    values.add(ingredientPriceRage[0]);
                    values.add(ingredientPriceRage[1]);
                    conditions.add("ingredient_price BETWEEN ? AND ?");
                }
                else {
                    throw new IllegalArgumentException(
                            ingredientPriceRage[1] +" must be greater than "+ ingredientPriceRage[1]
                    );
                }
            }
        }
        // Add conditions if any were added to the conditions list
        if (!conditions.isEmpty()) {
            sql += " AND " + String.join(" AND ", conditions);
        }
        // Add order conditions if provided
        List<String> orderConditions = new ArrayList<>();
        for (OrderCriteria orderCriteria : orderCriteriaList) {
            orderConditions.add(orderCriteria.getColumn() + " " + orderCriteria.getOrder().name());
        }
        if (!orderConditions.isEmpty()) {
            sql += " ORDER BY " + String.join(", ", orderConditions);
        }
        // Add LIMIT and OFFSET to sql
        sql += " LIMIT ? OFFSET ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            // Set pagination parameters
            statement.setInt(1+values.size(), size);
            statement.setInt(2+values.size(), size * (page - 1));
            // Set the other values from the conditions list by loop
            int index = 1;
            for (Object value : values) {
                statement.setObject(index++, value);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()){
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(resultSet.getString("name")); //ingredient name
                    filterIngredientList.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return filterIngredientList;
    }


    public List<Ingredient> FilteredPagedIngredientByPrice(
            List<FilterCriteria> filterCriteriaList,
            List<OrderCriteria> orderCriteriaList,
            int amount,
            int page, int size
    ){
        List<Ingredient> filterIngredientPrice = new ArrayList<>();
        String sql = "SELECT name\n" +
                "FROM ingredient\n" +
                "join ingredient_price_history iph on ingredient.id_ingredient = iph.ingredient_id\n" +
                "WHERE 1=1"; // Keep the base WHERE clause to pass even if there is AND
        List<String> conditionPrice = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but actual is " + page);
        }
        for(FilterCriteria filterCriteria : filterCriteriaList) {
            String column = filterCriteria.getColumn();
            Object value = filterCriteria.getValue();
            if ("name".equals(column)) {
                conditionPrice.add("name ILIKE ?");
                values.add("%" + value + "%");
            }
        }
        // Add conditionPrice if any were added to the conditionPrice list
        if (!conditionPrice.isEmpty()) {
            sql += " AND " + String.join(" AND ", conditionPrice) + " AND ingredient_price <= ?";
        }
        List<String> orderCondition = new ArrayList<>();
        for (OrderCriteria orderCriteria : orderCriteriaList) {
            orderCondition.add(orderCriteria.getColumn() + " " + orderCriteria.getOrder().name());
        }
        if (!orderCondition.isEmpty()) {
            sql += " ORDER BY " + String.join(", ", orderCondition);
        }
        // Add LIMIT and OFFSET to sql
        sql += " LIMIT ? OFFSET ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            // Set pagination parameters
            statement.setInt(1+values.size(), amount);
            statement.setInt(2+values.size(), size);
            statement.setInt(3+values.size(), size * (page - 1));
            // Set the other values from the conditions list by loop
            int index = 1;
            for (Object value : values) {
                statement.setObject(index++, value);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()){
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(resultSet.getString("name")); //ingredient name
                    filterIngredientPrice.add(ingredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return filterIngredientPrice;
    }

    @Override
    public Ingredient findById(int ingredientId) {
        Ingredient ingredient = null;
        String sql ="SELECT * FROM ingredient WHERE id_ingredient=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1,ingredientId);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    ingredient = new Ingredient();
                    ingredient.setIngredientId(resultSet.getInt("id_ingredient"));
                    ingredient.setName(resultSet.getString("name"));
                    ingredient.setUnity(unitMapper.fromResultSetDbValue(resultSet.getString("unity")));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ingredient;
    }

    @Override
    public List<Ingredient> showAll(int page, int size, OrderBy orderBy) {
        return List.of();
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Ingredient save(Ingredient models) {
        return null;
    }


}
