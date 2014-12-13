package org.javafp.javapickling.tutorial.picklers;

import org.javafp.javapickling.core.PicklerBase;
import org.javafp.javapickling.core.PicklerCore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickler<PF> extends PicklerBase<Date, PF> {

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public DatePickler(PicklerCore<PF> core) {
        super(core, Date.class);
    }

    @Override
    public PF pickle(Date date, PF target) throws Exception {
        return string_p().pickle(df.format(date), target);
    }

    @Override
    public Date unpickle(PF source) throws Exception {
        return df.parse(string_p().unpickle(source));
    }
}
