package ru.itmo.moona;

import java.time.Instant;

public final class StockMove {
    // Уникальный номер движения. Программа назначает сама.
    public long id;
    // К какой бутылке относится движение (id бутылки).
// Должен ссылаться на реально существующий ReagentBatch.
    public long batchId;
    // Тип движения: IN (приход), OUT (расход), DISCARD (списание).
    public StockMoveType type;
    // Количество (число). Должно быть > 0.
    public double quantity;
    // Единицы (берём из бутылки, чтобы не путаться).
    public BatchUnit unit;
    // Причина/комментарий (можно пусто). До 128 символов.
    public String reason;
    // Кто сделал движение (логин). На ранних этапах можно "SYSTEM".
    public String ownerUsername;
    // Время движения. Если не вводят — текущее.
    public Instant movedAt;
    // Когда запись создана. Программа ставит автоматически.
    public Instant createdAt;
}
