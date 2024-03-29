package com.orange.jiachen.landlords.entity;

import com.orange.jiachen.landlords.enums.PokerLevel;
import com.orange.jiachen.landlords.enums.PokerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Poke, with {@link PokerLevel} and {@link PokerType}
 *
 * @author nico
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Poker {

    private PokerLevel level;

    private PokerType type;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Poker other = (Poker) obj;
        if (level != other.level)
            return false;
        return type == other.type;
    }

    @Override
    public String toString() {
        return level.getLevel() + " ";
    }

}
